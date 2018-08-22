package org.ciakraa.wavelet.event;

import org.ciakraa.wavelet.common.CommonUnitTest;
import org.ciakraa.wavelet.web_api.SpotifyAuthorizationService;
import org.ciakraa.wavelet.web_api.SpotifyUnauthorizedException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.ciakraa.wavelet.event.EventConstants.POLL_COUNT;
import static org.mockito.Mockito.when;

public final class ListenedTrackPollUnitTest extends CommonUnitTest {

    @Mock
    private ListenedTrackService listenedTrackService;

    @Mock
    private SpotifyAuthorizationService authService;

    // Manually mock the interface to store tracks from publish(events) in a list that we can verify.
    private UserEventPublisher<ListenedTrack> eventPublisher;
    private List<ListenedTrack> publishedTracks;

    private ListenedTrackPoll target;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        publishedTracks = new ArrayList<>();
        eventPublisher = new UserEventPublisher<ListenedTrack>() {
            @Override
            public void publish(Collection<ListenedTrack> events) {
                events.stream().forEach(publishedTracks::add);
            }
        };

        target = new ListenedTrackPoll.Builder()
                .setUserCred(userCred)
                .setAuthService(authService)
                .setEventPublisher(eventPublisher)
                .setListenedTrackService(listenedTrackService)
                .build();
    }

    @Test
    public void shouldPublishNothingWhenUserRefreshThrowsException() throws SpotifyUnauthorizedException {
        when(authService.refreshUser(userCred)).thenThrow(new SpotifyUnauthorizedException());

        target.run();
        assertThat(publishedTracks).isEmpty();
    }

    @Test
    public void shouldPublishNothingWhenNoTracksAreReturned() throws SpotifyUnauthorizedException {
        when(authService.refreshUser(userCred)).thenReturn(Optional.of(userCred));
        when(listenedTrackService.getUniqueRecentlyListened(userCred, POLL_COUNT)).thenReturn(emptyList());

        target.run();
        assertThat(publishedTracks).isEmpty();
    }

    @Test
    public void shouldPublishTracks() throws SpotifyUnauthorizedException {
        when(authService.refreshUser(userCred)).thenReturn(Optional.of(userCred));
        when(listenedTrackService.getUniqueRecentlyListened(userCred, POLL_COUNT)).thenReturn(getListenedTracks());

        target.run();
        assertListenedTracks(publishedTracks);
    }
}
