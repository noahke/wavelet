package org.ciakraa.wavelet.event;

import org.ciakraa.wavelet.web_api.SpotifyAuthorizationService;
import org.ciakraa.wavelet.web_api.SpotifyUserCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

/**
 * This service will schedule a thread task to poll to Spotify for {@link ListenedTrack} once every poll period.
 */
@Service
public class ListenedTrackPollService {

    private final TaskScheduler taskScheduler;
    private final ListenedTrackService userEventService;
    private final SpotifyAuthorizationService authService;
    private final ListenedTrackPublisher eventPublisher;

    @Autowired
    ListenedTrackPollService(TaskScheduler taskScheduler, ListenedTrackService userEventService, SpotifyAuthorizationService authService,
                             ListenedTrackPublisher eventPublisher) {
        this.taskScheduler = taskScheduler;
        this.userEventService = userEventService;
        this.authService = authService;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Creates a recurring poll to fetch {@link ListenedTrack} events for a given user.
     *
     * @param userCredentials
     */
    public void addPolls(SpotifyUserCredentials userCredentials) {
        ListenedTrackPoll poll = buildPoll(userCredentials);
        taskScheduler.scheduleAtFixedRate(poll, EventConstants.POLL_PERIOD);
    }

    private ListenedTrackPoll buildPoll(SpotifyUserCredentials userCred) {
        return new ListenedTrackPoll.Builder()
                .setListenedTrackService(userEventService)
                .setAuthService(authService)
                .setEventPublisher(eventPublisher)
                .setUserCred(userCred)
                .build();
    }
}
