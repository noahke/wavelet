package org.ciakraa.wavelet.web_api.spring;

import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.exceptions.detailed.TooManyRequestsException;
import com.wrapper.spotify.exceptions.detailed.UnauthorizedException;
import com.wrapper.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;
import org.ciakraa.wavelet.web_api.SpotifyUnauthorizedException;
import org.ciakraa.wavelet.web_api.SpotifyUserCredentials;
import org.ciakraa.wavelet.web_api.SpotifyUserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * In this class, you'll see a lot of builders from the Spotify Web API Java library being intercepted via mockito.
 *
 * Each test's strategy is roughly the same: let the library do it's thing unimpeded,
 * but slip in a mocked HTTP manager or request object so we can ensure the logic is executing correctly.
 *
 * Because {@link DefaultAuthorizationService} uses {@link SpotifyApiFactory} for
 * building the library's request objects, this mocking is fairly straightforward (otherwise, we couldn't unit test),
 * though having to intercept multiple requests in a single test is a bit cumbersome.
 */
public final class DefaultAuthorizationServiceUnitTest extends AbstractWebApiMockRequestUnitTest {

    @Mock
    private SpotifyUserService userService;

    @Mock
    private RedisOperations<String, Object> redis;

    @Mock
    private BoundSetOperations<String, Object> states;

    private DefaultAuthorizationService target;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockRequests();
        mockRedis();

        target = new DefaultAuthorizationService(getClientCred(), apiFactory, redis, userService);
    }

    @Test
    public void shouldThrowExceptionWhenPassedInvalidAuthUriParams() {
        assertThatNullPointerException().isThrownBy(() -> target.requestAuthorizationUri(null, SCOPES));
        assertThatNullPointerException().isThrownBy(() -> target.requestAuthorizationUri(REDIRECT_URI, null));
    }

    @Test
    public void shouldReturnValidUriWhenUriRequestSucceeds() throws UnsupportedEncodingException {
        URI result = target.requestAuthorizationUri(REDIRECT_URI, SCOPES).get();
        MultiValueMap<String, String> queryParams = UriComponentsBuilder.fromUri(result).build().getQueryParams();
        assertThat(URLDecoder.decode(queryParams.getFirst("state"), "UTF-8").length()).isEqualTo(STATE_LENGTH);
        assertThat(URLDecoder.decode(queryParams.getFirst("redirect_uri"), "UTF-8")).isEqualTo(REDIRECT_URI.toString());
        assertThat(URLDecoder.decode(queryParams.getFirst("show_dialog"), "UTF-8")).isEqualTo(Boolean.TRUE.toString());
        assertThat(URLDecoder.decode(queryParams.getFirst("client_id"), "UTF-8")).isEqualTo(CLIENT_ID);
        assertThat(URLDecoder.decode(queryParams.getFirst("scope"), "UTF-8")).isEqualTo(SCOPES);
    }

    @Test
    public void shouldReturnEmptyCredWhenUserAuthIsPassedInvalidArgs() {
        assertThatNullPointerException().isThrownBy(() -> target.authorizeUser(null, AUTHORIZATION_CODE, STATE));
        assertThatNullPointerException().isThrownBy(() -> target.authorizeUser(REDIRECT_URI, null, STATE));
        assertThatNullPointerException().isThrownBy(() -> target.authorizeUser(REDIRECT_URI, AUTHORIZATION_CODE, null));
    }

    @Test
    public void shouldReturnEmptyCredWhenUserAuthIsPassedInvalidState() {
        when(states.isMember(anyString())).thenReturn(false);

        assertThatIllegalArgumentException().isThrownBy(() -> target.authorizeUser(REDIRECT_URI, AUTHORIZATION_CODE, STATE));
    }

    @Test
    public void shouldReturnEmptyCredWhenUserAuthCodeRequestFailsWithSpotifyWebApiException() throws Exception  { ;
        when(codeReq.execute()).thenThrow(new SpotifyWebApiException());

        Optional<SpotifyUserCredentials> result = target.authorizeUser(REDIRECT_URI, AUTHORIZATION_CODE, STATE);
        assertThat(result).isEmpty();
    }

    @Test
    public void shouldReturnEmptyCredWhenUserAuthCodeRequestFailsWithTooManyRequestsException() throws Exception {
        when(codeReq.execute()).thenThrow(new TooManyRequestsException("Try after 0 seconds", 0));

        Optional<SpotifyUserCredentials> result = target.authorizeUser(REDIRECT_URI, AUTHORIZATION_CODE, STATE);
        assertThat(result).isEmpty();
    }

    @Test
    public void shouldThrowExceptionWhenUserAuthCodeRequestFailsWithUnauthorizedException() throws Exception {
        when(codeReq.execute()).thenThrow(new UnauthorizedException());

        assertThatExceptionOfType(SpotifyUnauthorizedException.class)
                .isThrownBy(() ->target.authorizeUser(REDIRECT_URI, AUTHORIZATION_CODE, STATE));
    }

    @Test
    public void shouldReturnEmptyCredWhenUserAuthProfileRequestFailsWithSpotifyWebApiException() throws Exception  {
        mockHttpManager();
        when(apiFactory.build(any(GetCurrentUsersProfileRequest.Builder.class))).thenReturn(userReq);
        when(userReq.execute()).thenThrow(new SpotifyWebApiException());

        Optional<SpotifyUserCredentials> result = target.authorizeUser(REDIRECT_URI, AUTHORIZATION_CODE, STATE);
        assertThat(result).isEmpty();
    }

    @Test
    public void shouldReturnEmptyCredWhenUserAuthProfileRequestFailsWithTooManyRequestsException() throws Exception  {
        mockHttpManager();
        when(apiFactory.build(any(GetCurrentUsersProfileRequest.Builder.class))).thenReturn(userReq);
        when(userReq.execute()).thenThrow(new TooManyRequestsException("Try after 0 second", 0));

        Optional<SpotifyUserCredentials> result = target.authorizeUser(REDIRECT_URI, AUTHORIZATION_CODE, STATE);
        assertThat(result).isEmpty();
    }

    @Test
    public void shouldReturnValidCredWhenUserAuthSuceeds()  throws SpotifyUnauthorizedException  {
        mockHttpManager();

        SpotifyUserCredentials result = target.authorizeUser(REDIRECT_URI, AUTHORIZATION_CODE, STATE).get();
        assertThat(result.getUserId()).isEqualTo(USER_ID);
        assertThat(result.getUserDisplayName()).isEqualTo(USER_DISPLAY_NAME);
        assertThat(result.getAccessToken()).isEqualTo(ACCESS_TOKEN);
        assertThat(result.getRefreshToken()).isEqualTo(REFRESH_TOKEN);
        assertThat(result.getClientCred().validate()).isTrue();
    }

    @Test
    public void shouldReturnEmptyCredWhenUserRefreshIsPassedInvalidArgs()  {
        assertThatNullPointerException().isThrownBy(() -> target.refreshUser(null));
        assertThatIllegalArgumentException().isThrownBy(() -> target.refreshUser(mock(SpotifyUserCredentials.class)));
    }

    @Test
    public void shouldReturnEmptyCredWhenUserRefreshFailsWithExceptions() throws Exception  {
        when(refreshReq.execute()).thenThrow(new SpotifyWebApiException());

        Optional<SpotifyUserCredentials> result = target.refreshUser(getUserCred());
        assertThat(result).isEmpty();
    }

    @Test
    public void shouldReturnValidCredWhenUserRefreshSuceeds() throws SpotifyUnauthorizedException {
        mockHttpManager();

        SpotifyUserCredentials result = target.refreshUser(getUserCred()).get();
        assertThat(result.getUserId()).isEqualTo(USER_ID);
        assertThat(result.getUserDisplayName()).isEqualTo(USER_DISPLAY_NAME);
        assertThat(result.getAccessToken()).isEqualTo(REFRESHED_ACCESS_TOKEN);
        assertThat(result.getRefreshToken()).isEqualTo(REFRESH_TOKEN);
        assertThat(result.getClientCred().validate()).isTrue();
    }

    private void mockRedis() {
        when(redis.boundSetOps(STATES_KEY)).thenReturn(states);
        when(states.isMember(anyString())).thenReturn(true);
    }


}

