package org.ciakraa.wavelet.web_api;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import com.wrapper.spotify.requests.data.player.GetCurrentUsersRecentlyPlayedTracksRequest;
import com.wrapper.spotify.requests.data.tracks.GetAudioFeaturesForSeveralTracksRequest;
import com.wrapper.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;
import org.springframework.stereotype.Service;

/**
 * This factory exists solely to abstract away the creation of SpotifyApi and request objects provided by the Spotify Web API Java.
 *
 * This allows us to easily mock SpotifyApi interactions for unit tests, as the Spotify Web API Java library
 * does not allow its builders to be mocked due to a final modifier.
 *
 */
@Service
class SpotifyApiFactory {

    SpotifyApi build(SpotifyApi.Builder builder) {
        return builder.build();
    }

    AuthorizationCodeUriRequest build(AuthorizationCodeUriRequest.Builder builder) {
        return builder.build();
    }

    AuthorizationCodeRequest build(AuthorizationCodeRequest.Builder builder) {
        return builder.build();
    }

    AuthorizationCodeRefreshRequest build(AuthorizationCodeRefreshRequest.Builder builder) {
        return builder.build();
    }

    GetCurrentUsersProfileRequest build(GetCurrentUsersProfileRequest.Builder builder) {
        return builder.build();
    }

    GetCurrentUsersRecentlyPlayedTracksRequest build(GetCurrentUsersRecentlyPlayedTracksRequest.Builder builder) {
        return builder.build();
    }

    GetAudioFeaturesForSeveralTracksRequest build(GetAudioFeaturesForSeveralTracksRequest.Builder builder) {
        return builder.build();
    }
}
