package org.ciakraa.wavelet.event;

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
final class ListenedTrackPoll implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(ListenedTrackPollService.class);

    private final ListenedTrackService listenedTrackService;
    private final UserEventPublisher<ListenedTrack> eventPublisher;
    private final SpotifyAuthorizationService authService;

    private SpotifyUserCredentials userCred;

    private ListenedTrackPoll(Builder builder) {
        this.listenedTrackService = builder.listenedTrackService;
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

        List<ListenedTrack> tracks = listenedTrackService.getUniqueRecentlyListened(userCred, EventConstants.POLL_COUNT);
        if (tracks.isEmpty()) {
            return;
        }

        eventPublisher.publish(tracks);
    }

    static final class Builder {
        private ListenedTrackService listenedTrackService;
        private SpotifyAuthorizationService authService;
        private UserEventPublisher<ListenedTrack> eventPublisher;
        private SpotifyUserCredentials userCred;

        public Builder setListenedTrackService(ListenedTrackService listenedTrackService) {
            this.listenedTrackService = listenedTrackService;
            return this;
        }

        public Builder setAuthService(SpotifyAuthorizationService authService) {
            this.authService = authService;
            return this;
        }

        public Builder setEventPublisher(UserEventPublisher<ListenedTrack> eventPublisher) {
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
