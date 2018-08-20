package org.ciakraa.wavelet.event.spring;

import org.ciakraa.wavelet.event.ListenedTrack;
import org.ciakraa.wavelet.event.PollService;
import org.ciakraa.wavelet.event.EventService;
import org.ciakraa.wavelet.web_api.SpotifyAuthorizationService;
import org.ciakraa.wavelet.web_api.SpotifyUserCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

/**
 * This {@link PollService} will schedule a thread task to poll to Spotify for {@link ListenedTrack} once every poll period.
 */
@Service
final class ListenedTrackPollService implements PollService<ListenedTrack>, EventConstants {

    private final TaskScheduler taskScheduler;
    private final EventService eventService;
    private final SpotifyAuthorizationService authService;
    private final ListenedTrackPublisher eventPublisher;

    @Autowired
    ListenedTrackPollService(TaskScheduler taskScheduler, EventService eventService, SpotifyAuthorizationService authService,
                             ListenedTrackPublisher eventPublisher) {
        this.taskScheduler = taskScheduler;
        this.eventService = eventService;
        this.authService = authService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void addPolls(SpotifyUserCredentials userCredentials) {
        ListenedTrackPoll poll = buildPoll(userCredentials);
        taskScheduler.scheduleAtFixedRate(poll, POLL_PERIOD);
    }

    private ListenedTrackPoll buildPoll(SpotifyUserCredentials userCred) {
        return new ListenedTrackPoll.Builder()
                .setEventService(eventService)
                .setAuthService(authService)
                .setEventPublisher(eventPublisher)
                .setUserCred(userCred)
                .build();
    }
}
