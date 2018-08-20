package org.ciakraa.wavelet.web_api;

import java.net.URI;
import java.util.Optional;

/**
 * Interface for authorizing requests with Spotify's Web API Service.
 *
 * Responsible for the Authorization Code Flow, which depends on a 4 step process:
 * 1. Request an authorization URL from Spotify, using the app's client id, secret, and callback url.
 * 2. Once a User authorizes the app via the authorization URL, they are redirected to the callback url with a code and state param.
 * 3. The app uses the code to request an access token for querying user data from Spotify's API, and a refresh token.
 * 4. The app uses the refresh token to request a new access token (they expire quickly).
 *
 * @see <a href="https://beta.developer.spotify.com/documentation/general/guides/authorization-guide/#authorization-code-flow">Spotify: Authorization Code Flow</a>
 */
public interface SpotifyAuthorizationService {

    /**
     * Builds the URI that a user will follow to authorize the app.
     *
     * Optionals are returned if an IOException occurs during URI build.
     */
    Optional<URI> requestAuthorizationUri(URI redirectUri, String scopes);

    /**
     * Retrieves access and refresh tokens for a user, then fetches their profile, and combines all into SpotifyUserCredentials.
     *
     * Optionals are returned whenever a call to Spotify fails. Only the following HTTP response codes are handled specially:
     *
     * 401: {@link SpotifyUnauthorizedException} is thrown if Spotify API requires an access token for a request.
     * Since these methods are concerned with authorization, the exception would likely mean the app's client id/secret are invalid.
     *
     * 429: "Rate Limiting Has Been applied". We will sleep the thread for the specified time and try again, for a
     * maximum of two tries. If that still fails, an empty Optional is returned.
     *
     * Null args and/or invalid credentials throw an NPE.
     */
    Optional<SpotifyUserCredentials> authorizeUser(URI redirectUri, String code, String state) throws SpotifyUnauthorizedException;

    /**
     * Access tokens expire after a short period of time. Thus, you must request a new token from Spotify using
     * the refresh token. This method will return a new SpotifyUserCredentials with the latest access and refresh tokens;
     * the userCred passed into the method will no longer be valid!
     *
     * It's suggested to execute this method before every call to {@link SpotifyActivityService}, so you are guaranteed
     * to have working tokens.
     *
     * Optionals are returned whenever a call to Spotify fails. Only the following HTTP response codes are handled specially:
     *
     * 401: {@link SpotifyUnauthorizedException} is thrown if Spotify API requires an access token for a request.
     * Since these methods are concerned with authorization, the exception would likely mean the app's client id/secret are invalid.
     *
     * 429: "Rate Limiting Has Been applied". We will sleep the thread for the specified time and try again, for a
     * maximum of two tries. If that still fails, an empty Optional is returned.
     *
     * Null args and/or invalid credentials throws an NPE.
     */
    Optional<SpotifyUserCredentials> refreshUser(SpotifyUserCredentials userCred) throws SpotifyUnauthorizedException;

}
