package org.ciakraa.wavelet.web_api;

import com.wrapper.spotify.model_objects.specification.AudioFeatures;
import com.wrapper.spotify.model_objects.specification.PlayHistory;

import java.util.List;

/**
 * Responsible for querying Spotify Web API and returning events.
 *
 * You will need to have created a {@link SpotifyClientCredentials} for any API request,
 * and {@link SpotifyUserCredentials} for any API request involving user data.
 *
 */
public interface SpotifyActivityService {

    /**
     * Fetches up to 50 most recently listened tracks for a given user.
     * Will return an empty list if an error is encountered while querying Spotify.
     *
     * @see <a href="https://beta.developer.spotify.com/documentation/web-api/reference/player/get-recently-played/">Get Current User's Recently Played Tracks</a>
     */
    List<PlayHistory> getRecentlyListened(SpotifyUserCredentials userCred, int count) throws SpotifyUnauthorizedException;

    /**
     * Fetches up to 50 most recently listened tracks for a given user, since a unix timestamp.
     * Will return an empty list if an error is encountered while querying Spotify.
     *
     * @see <a href="https://beta.developer.spotify.com/documentation/web-api/reference/player/get-recently-played/">Get Current User's Recently Played Tracks</a>
     */
    List<PlayHistory> getRecentlyListened(SpotifyUserCredentials userCred, int count, long beforeTimestamp) throws SpotifyUnauthorizedException;

    /**
     * Fetches Audio Features for given track ids.
     * Will return an empty set if an error is encountered while querying Spotify.
     *
     * @see <a href="https://beta.developer.spotify.com/documentation/web-api/reference/tracks/get-several-tracks/">Get Several Tracks </a>
     */
    List<AudioFeatures> getAudioFeatures(SpotifyUserCredentials userCred, List<String> trackIds) throws SpotifyUnauthorizedException;
}
