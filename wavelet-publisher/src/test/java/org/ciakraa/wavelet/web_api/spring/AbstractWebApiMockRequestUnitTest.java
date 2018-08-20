package org.ciakraa.wavelet.web_api.spring;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import com.wrapper.spotify.requests.data.player.GetCurrentUsersRecentlyPlayedTracksRequest;
import com.wrapper.spotify.requests.data.tracks.GetAudioFeaturesForSeveralTracksRequest;
import com.wrapper.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Many tests rely on mocking request builds + executions, so we collect common
 * mocking functionality here.
 */
abstract class AbstractWebApiMockRequestUnitTest extends AbstractWebApiUnitTest {

    @Mock
    SpotifyApiFactory apiFactory;

    @Mock
    AuthorizationCodeRequest codeReq;

    @Mock
    GetCurrentUsersProfileRequest userReq;

    @Mock
    AuthorizationCodeRefreshRequest refreshReq;

    @Mock
    GetCurrentUsersRecentlyPlayedTracksRequest recentTracksReq;

    @Mock
    GetAudioFeaturesForSeveralTracksRequest audioFeaturesReq;

    // We often rely on intercepting requests via DefaultSpotifyApiFactory so we can inject our mocks when needed.
    // We're fine using the SpotifyApi and AuthorizationCodeUriRequest though, since they're only used for builders.
    void mockRequests() {
        when(apiFactory.build(any(SpotifyApi.Builder.class))).thenCallRealMethod();
        when(apiFactory.build(any(AuthorizationCodeUriRequest.Builder.class))).thenCallRealMethod();
        when(apiFactory.build(any(AuthorizationCodeRequest.Builder.class))).thenReturn(codeReq);
        when(apiFactory.build(any(GetCurrentUsersProfileRequest.Builder.class))).thenReturn(userReq);
        when(apiFactory.build(any(AuthorizationCodeRefreshRequest.Builder.class))).thenReturn(refreshReq);
        when(apiFactory.build(any(GetCurrentUsersRecentlyPlayedTracksRequest.Builder.class))).thenReturn(recentTracksReq);
        when(apiFactory.build(any(GetAudioFeaturesForSeveralTracksRequest.Builder.class))).thenReturn(audioFeaturesReq);
    }

    // When we want to test the requests without mock request objects, we just need to mock the http manager
    // so we can return test json. This will override mockRequests().
    void mockHttpManager() {
        when(apiFactory.build(any(AuthorizationCodeRequest.Builder.class))).then(answer -> {
            AuthorizationCodeRequest.Builder builder = answer.getArgument(0);
            return builder.setHttpManager(MockedHttpManager.returningJson(AUTHORIZATION_CODE_JSON)).build();
        });
        when(apiFactory.build(any(GetCurrentUsersProfileRequest.Builder.class))).then(answer -> {
            GetCurrentUsersProfileRequest.Builder builder = answer.getArgument(0);
            return builder.setHttpManager(MockedHttpManager.returningJson(USER_PROFILE_JSON)).build();
        });
        when(apiFactory.build(any(AuthorizationCodeRefreshRequest.Builder.class))).then(answer -> {
            AuthorizationCodeRefreshRequest.Builder builder = answer.getArgument(0);
            return builder.setHttpManager(MockedHttpManager.returningJson(AUTHORIZATION_REFRESH_JSON)).build();
        });
        when(apiFactory.build(any(GetCurrentUsersRecentlyPlayedTracksRequest.Builder.class))).then(answer -> {
            GetCurrentUsersRecentlyPlayedTracksRequest.Builder builder = answer.getArgument(0);
            return builder.setHttpManager(MockedHttpManager.returningJson(RECENTLY_PLAYED_JSON)).build();
        });
        when(apiFactory.build(any(GetAudioFeaturesForSeveralTracksRequest.Builder.class))).then(answer -> {
            GetAudioFeaturesForSeveralTracksRequest.Builder builder = answer.getArgument(0);
            return builder.setHttpManager(MockedHttpManager.returningJson(AUDIO_FEATURES)).build();
        });
    }


}
