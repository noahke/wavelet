package org.ciakraa.wavelet.web_api.spring;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.model_objects.specification.User;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import com.wrapper.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.Validate;
import org.ciakraa.wavelet.web_api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Optional;

/**
 * A wrapper for the Spotify Web API Java library developed by {@author thelinmichael}.
 *
 * Responsible for the Authorization Code Flow, which depends on a 4 step process:
 * 1. Request an authorization URL from Spotify, using the app's client id, secret, a randomly generated state, and callback url.
 * 2. Once a User authorizes the app via the authorization URL, they are redirected to the callback url with a code param and state value.
 * 3. The app uses the code to request access and refresh tokens for querying user data from Spotify's API.
 * 4. The app uses the refresh token to request new tokens after a given period (1 hour).
 *
 * {@link SpotifyUnauthorizedException} is thrown if Spotify API requires an access token for a request, and the req fails with
 * a 401 status. Since these methods are concerned with initial authorization, the exception would likely mean the app's client id/secret are invalid.
 *
 * @see <a href="https://beta.developer.spotify.com/documentation/general/guides/authorization-guide/#authorization-code-flow">Spotify: Authorization Code Flow</a>
 * @see <a href="https://github.com/thelinmichael/spotify-web-api-java#authorization-code-flow">Spotify Web API Java: Authorization Code Flow</a>
 */
@Service
final class DefaultAuthorizationService extends AbstractSpotifyApiService implements SpotifyAuthorizationService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultAuthorizationService.class);

    private final SpotifyClientCredentials clientCred;
    private final SpotifyApiFactory apiFactory;
    private final SpotifyUserService userService;
    private final BoundSetOperations<String, Object> states;

    @Autowired
    DefaultAuthorizationService(SpotifyClientCredentials clientCred,
                                SpotifyApiFactory apiFactory,
                                RedisOperations<String, Object> redis,
                                SpotifyUserService userService) {
        this.clientCred = clientCred;
        this.apiFactory = apiFactory;
        this.states = redis.boundSetOps(STATES_KEY);
        this.userService = userService;
    }

    @Override
    public Optional<URI> requestAuthorizationUri(URI redirectUri, String scopes) {
        Validate.notNull(redirectUri);
        Validate.notBlank(scopes);

        SpotifyApi.Builder apiBuilder = new SpotifyApi.Builder()
                .setClientId(clientCred.getClientId())
                .setClientSecret(clientCred.getClientSecret())
                .setRedirectUri(redirectUri);
        SpotifyApi spotifyApi = apiFactory.build(apiBuilder);

        AuthorizationCodeUriRequest.Builder uriRequestBuilder = spotifyApi.authorizationCodeUri()
                .state(generateState())
                .scope(String.join(",", scopes))
                .show_dialog(true);
        AuthorizationCodeUriRequest req = apiFactory.build(uriRequestBuilder);

        Optional<URI> uri = executeWithoutAccess(req::execute);

        return uri;
    }

    @Override
    public Optional<SpotifyUserCredentials> authorizeUser(URI redirectUri, String code, String state) throws SpotifyUnauthorizedException {
        Validate.isTrue(validateState(state));
        Validate.notNull(code);
        Validate.notNull(redirectUri);

        SpotifyApi.Builder apiBuilder = new SpotifyApi.Builder()
                .setClientId(clientCred.getClientId())
                .setClientSecret(clientCred.getClientSecret())
                .setRedirectUri(redirectUri);
        SpotifyApi spotifyApi = apiFactory.build(apiBuilder);

        AuthorizationCodeRequest.Builder codeRequestBuilder = spotifyApi.authorizationCode(code);
        AuthorizationCodeRequest codeRequest = apiFactory.build(codeRequestBuilder);

        Optional<AuthorizationCodeCredentials> authCred = executeWithAccess(codeRequest::execute);
        if (!authCred.isPresent()) {
            LOG.error("Failed to authorize user with client credentials: {}, code: {}, state: {}", clientCred, code, state);
            return Optional.empty();
        }
        Optional<User> user = getUser(authCred.get());
        if (!user.isPresent()) {
            LOG.error("Failed to get user profile with client credentials: {}, code: {}, state: {}", clientCred, code, state);
            return Optional.empty();
        }

        SpotifyUserCredentials userCred = new DefaultSpotifyUserCredentials.Builder()
                .setUserId(user.get().getId())
                .setUserDisplayName(user.get().getDisplayName())
                .setAccessToken(authCred.get().getAccessToken())
                .setRefreshToken(authCred.get().getRefreshToken())
                .setClientCred(clientCred)
                .build();

        userService.save(userCred);
        deleteState(state);

        LOG.info("Successfully created user credentials: {}", userCred);
        return Optional.of(userCred);
    }

    @Override
    public Optional<SpotifyUserCredentials> refreshUser(SpotifyUserCredentials userCred) throws SpotifyUnauthorizedException {
        Validate.isTrue(userCred.validate());

        SpotifyApi.Builder apiBuilder = new SpotifyApi.Builder()
                .setClientId(clientCred.getClientId())
                .setClientSecret(clientCred.getClientSecret())
                .setRefreshToken(userCred.getRefreshToken());
        SpotifyApi spotifyApi = apiFactory.build(apiBuilder);

        AuthorizationCodeRefreshRequest.Builder refreshRequestBuilder = spotifyApi.authorizationCodeRefresh();
        AuthorizationCodeRefreshRequest refreshRequest = apiFactory.build(refreshRequestBuilder);

        Optional<AuthorizationCodeCredentials> authCred = executeWithAccess(refreshRequest::execute);
        if (!authCred.isPresent()) {
            LOG.error("Failed to refresh user credentials for: {}", userCred);
            return Optional.empty();
        }

        SpotifyUserCredentials refreshedUserCred = new DefaultSpotifyUserCredentials.Builder()
                .setUserId(userCred.getUserId())
                .setUserDisplayName(userCred.getUserDisplayName())
                .setAccessToken(authCred.get().getAccessToken())
                .setRefreshToken(userCred.getRefreshToken())
                .setClientCred(clientCred)
                .build();

        userService.save(userCred);

        return Optional.of(refreshedUserCred);
    }

    /**
     * In addition to access tokens, we want the user's id and display name, which are necessary for model objects.
     */
    private Optional<User> getUser(AuthorizationCodeCredentials authCred) throws SpotifyUnauthorizedException {
        SpotifyApi.Builder apiBuilder = new SpotifyApi.Builder().setAccessToken(authCred.getAccessToken());
        SpotifyApi spotifyApi = apiFactory.build(apiBuilder);

        GetCurrentUsersProfileRequest.Builder profileReqBuilder = spotifyApi.getCurrentUsersProfile();
        GetCurrentUsersProfileRequest profileReq = apiFactory.build(profileReqBuilder);
        Optional<User> user = executeWithAccess(profileReq::execute);

        return user;
    }

    /**
     * State codes are accepted and subsequently returned by Spotify during the Authorization Code Flow.
     * A state code signifies that an incoming AuthorizationCodeCredentials is valid.
     */
    private String generateState() {
        String state = RandomStringUtils.randomAlphanumeric(STATE_LENGTH).toUpperCase();
        states.add(STATES_KEY, state);
        return state;
    }

    private boolean validateState(String state) {
        Validate.notNull(state);
        return states.isMember(state);
    }

    private void deleteState(String state) {
        states.remove(state);
    }

}
