package org.ciakraa.wavelet.event.spring;

import org.ciakraa.wavelet.event.EventService;
import org.ciakraa.wavelet.event.ListenedTrack;
import org.ciakraa.wavelet.event.EventPublisher;
import org.ciakraa.wavelet.web_api.SpotifyAuthorizationService;
import org.ciakraa.wavelet.web_api.SpotifyUnauthorizedException;
import org.ciakraa.wavelet.web_api.SpotifyUserCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * This poll will request recent {@link ListenedTrack} from Spotify every 10 minutes,
 * then publish each track to a kafka topic (assuming it has not been published yet).
 *
 * User credentials are refreshed with each poll to guarantee access to Spotify Web API.
 */
final class ListenedTrackPoll implements Runnable, EventConstants {

    private static final Logger LOG = LoggerFactory.getLogger(ListenedTrackPollService.class);

    private final EventService eventService;
    private final EventPublisher<ListenedTrack> eventPublisher;
    private final SpotifyAuthorizationService authService;

    private SpotifyUserCredentials userCred;

    ListenedTrackPoll(Builder builder) {
        this.eventService = builder.eventService;
        this.authService = builder.authService;
        this.eventPublisher = builder.eventPublisher;
        this.userCred = builder.userCred;
    }

    @Override
    public void run() {
        LOG.info("Polling listened tracks for user {}", userCred);

        try {
             authService.refreshUser(userCred).ifPresent(cred -> this.userCred = cred);
        } catch (SpotifyUnauthorizedException e) {
            LOG.warn("User cred is invalid and cannot be refreshed: {}", userCred);
            return;
        }

        List<ListenedTrack> tracks = eventService.getUniqueRecentlyListened(userCred, POLL_COUNT);
        if (tracks.isEmpty()) {
            return;
        }

        eventPublisher.publish(tracks);
    }

    static final class Builder {
        private EventService eventService;
        private SpotifyAuthorizationService authService;
        private EventPublisher<ListenedTrack> eventPublisher;
        private SpotifyUserCredentials userCred;

        public Builder setEventService(EventService eventService) {
            this.eventService = eventService;
            return this;
        }

        public Builder setAuthService(SpotifyAuthorizationService authService) {
            this.authService = authService;
            return this;
        }

        public Builder setEventPublisher(EventPublisher<ListenedTrack> eventPublisher) {
            this.eventPublisher = eventPublisher;
            return this;
        }

        public Builder setUserCred(SpotifyUserCredentials userCred) {
            this.userCred = userCred;
            return this;
        }

        public ListenedTrackPoll build() {
            return new ListenedTrackPoll(this);
        }
    }

}
