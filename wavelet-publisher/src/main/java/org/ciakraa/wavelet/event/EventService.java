package org.ciakraa.wavelet.event;

import org.ciakraa.wavelet.web_api.SpotifyUserCredentials;

import java.util.List;

/**
 * Service responsible for fetching and processing a {@link UserEvent}.
 * The service is typically called in a recurring fashion by a Spotify poll created by {@link PollService}.
 */
public interface EventService {

    /**
     * Retrieves the n latest {@link ListenedTrack} for a given Spotify user.
     *
     * Only new events are fetched; if we have already seen an event (thanks to previous polling), we will not
     * return it again.
     *
     * @param userCredentials
     * @param count
     * @return
     */
    List<ListenedTrack> getUniqueRecentlyListened(SpotifyUserCredentials userCredentials, int count);

}
