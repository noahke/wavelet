package org.ciakraa.wavelet.web_api.spring;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import com.wrapper.spotify.requests.data.player.GetCurrentUsersRecentlyPlayedTracksRequest;
import com.wrapper.spotify.requests.data.tracks.GetAudioFeaturesForSeveralTracksRequest;
import com.wrapper.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;

/**
 * This factory exists solely to abstract away the creation of SpotifyApi and request objects provided by the Spotify Web API Java.
 *
 * This allows us to easily mock SpotifyApi interactions for unit tests; otherwise, we'd be limited to integration tests, which
 * the Authorization Code Flow makes fairly difficult, as it requires users logging in.
 */
interface SpotifyApiFactory {

    default SpotifyApi build(SpotifyApi.Builder builder) {
        return builder.build();
    };

    default AuthorizationCodeUriRequest build(AuthorizationCodeUriRequest.Builder builder) {
        return builder.build();
    };

    default AuthorizationCodeRequest build(AuthorizationCodeRequest.Builder builder) {
        return builder.build();
    };

    default AuthorizationCodeRefreshRequest build(AuthorizationCodeRefreshRequest.Builder builder) {
        return builder.build();
    };

    default GetCurrentUsersProfileRequest build(GetCurrentUsersProfileRequest.Builder builder) {
        return builder.build();
    }

    default GetCurrentUsersRecentlyPlayedTracksRequest build(GetCurrentUsersRecentlyPlayedTracksRequest.Builder builder) {
        return builder.build();
    }

    default GetAudioFeaturesForSeveralTracksRequest build(GetAudioFeaturesForSeveralTracksRequest.Builder builder) {
        return builder.build();
    }
}
