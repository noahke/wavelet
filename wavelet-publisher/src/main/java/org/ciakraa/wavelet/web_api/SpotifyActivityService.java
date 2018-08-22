package org.ciakraa.wavelet.web_api;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.specification.AudioFeatures;
import com.wrapper.spotify.model_objects.specification.PagingCursorbased;
import com.wrapper.spotify.model_objects.specification.PlayHistory;
import com.wrapper.spotify.requests.data.player.GetCurrentUsersRecentlyPlayedTracksRequest;
import com.wrapper.spotify.requests.data.tracks.GetAudioFeaturesForSeveralTracksRequest;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

/**
 * Responsible for querying Spotify Web API and returning events.
 *
 * You will need to have created a {@link SpotifyClientCredentials} for any API request,
 * and {@link SpotifyUserCredentials} for any API request involving user data.
 *
 * @see <a href="https://beta.developer.spotify.com/documentation/web-api/reference/">Spotify Web API Reference</a>
 * @see <a href="https://github.com/thelinmichael/spotify-web-api-java">Spotify Web API Java</a>
 */
@Service
public class SpotifyActivityService extends AbstractSpotifyApiService {

    private static final Logger LOG = LoggerFactory.getLogger(SpotifyActivityService.class);

    // SpotifyApiFactory is used to better facilitate mocking in unit tests; it adds no functionality here.
    private final SpotifyApiFactory apiFactory;

    @Autowired
    SpotifyActivityService(SpotifyApiFactory apiFactory) {
        this.apiFactory = apiFactory;
    }

    /**
     * Fetches up to 50 most recently listened tracks for a given user.
     * Will return an empty list if an error is encountered while querying Spotify.
     *
     * @see <a href="https://beta.developer.spotify.com/documentation/web-api/reference/player/get-recently-played/">Get Current User's Recently Played Tracks</a>
     */
    public List<PlayHistory> getRecentlyListened(SpotifyUserCredentials userCred, int count)
            throws SpotifyUnauthorizedException {
        return getRecentlyListened(userCred, count, 0);
    }

    /**
     * Fetches up to 50 most recently listened tracks for a given user, since a unix timestamp.
     * Will return an empty list if an error is encountered while querying Spotify.
     *
     * @see <a href="https://beta.developer.spotify.com/documentation/web-api/reference/player/get-recently-played/">Get Current User's Recently Played Tracks</a>
     */
    public List<PlayHistory> getRecentlyListened(SpotifyUserCredentials userCred, int count, long beforeTimestamp) throws SpotifyUnauthorizedException {
        Validate.isTrue(userCred.validate());
        Validate.inclusiveBetween(1, WebApiConstants.MAX_RECENTLY_LISTENED_TO, count);
        Validate.isTrue(beforeTimestamp >= 0);

        SpotifyApi.Builder apiBuilder = new SpotifyApi.Builder().setAccessToken(userCred.getAccessToken());
        SpotifyApi spotifyApi = apiFactory.build(apiBuilder);

        GetCurrentUsersRecentlyPlayedTracksRequest.Builder recentlyPlayedBuilder = spotifyApi.getCurrentUsersRecentlyPlayedTracks().limit(count);
        if (beforeTimestamp > 0) {
            recentlyPlayedBuilder = recentlyPlayedBuilder.before(new Date(beforeTimestamp));
        }
        GetCurrentUsersRecentlyPlayedTracksRequest recentlyPlayedRequest = apiFactory.build(recentlyPlayedBuilder);

        Optional<PagingCursorbased<PlayHistory>> playHistory = executeWithAccess(recentlyPlayedRequest::execute);
        if (!playHistory.isPresent()) {
            LOG.error("Unable to retrieve recently listened tracks for user: {}", userCred);
            return emptyList();
        }

        return Arrays.stream(playHistory.get().getItems()).collect(toList());
    }

    /**
     * Fetches Audio Features for given track ids.
     * Will return an empty set if an error is encountered while querying Spotify.
     *
     * @see <a href="https://beta.developer.spotify.com/documentation/web-api/reference/tracks/get-several-tracks/">Get Several Tracks </a>
     */
    public List<AudioFeatures> getAudioFeatures(SpotifyUserCredentials userCred, List<String> trackIds)
            throws SpotifyUnauthorizedException {
        Validate.isTrue(userCred.validate());
        Validate.notEmpty(trackIds);

        SpotifyApi.Builder apiBuilder = new SpotifyApi.Builder().setAccessToken(userCred.getAccessToken());
        SpotifyApi spotifyApi = apiFactory.build(apiBuilder);

        GetAudioFeaturesForSeveralTracksRequest.Builder audioFeaturesBuilder = spotifyApi
                .getAudioFeaturesForSeveralTracks(trackIds.toArray(new String[trackIds.size()]));
        GetAudioFeaturesForSeveralTracksRequest audioFeaturesReq = apiFactory.build(audioFeaturesBuilder);

        Optional<Object[]> audioFeatures = executeWithAccess(audioFeaturesReq::execute);
        if (!audioFeatures.isPresent()) {
            LOG.error("Unable to retrieve featured tracks for user: {}, with tracks: {}", userCred, trackIds);
            return emptyList();
        }

        return Arrays.stream(audioFeatures.get()).map(af -> (AudioFeatures) af).collect(toList());

    }

}
