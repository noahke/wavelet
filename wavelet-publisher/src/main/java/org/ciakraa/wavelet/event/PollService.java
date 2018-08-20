package org.ciakraa.wavelet.event;

import org.ciakraa.wavelet.web_api.SpotifyUserCredentials;

/**
 * Responsible for managing the polls of {@link UserEvent}.
 */
public interface PollService<T extends UserEvent> {

    /**
     * Creates a recurring poll to fetch events for a given user.
     * Polling period depends on the implementation; expect polling every 10 minutes via a Task Scheduler.
     *
     * @param userCredentials
     */
    void addPolls(SpotifyUserCredentials userCredentials);
}
