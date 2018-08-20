package org.ciakraa.wavelet.event.spring;

import org.ciakraa.wavelet.event.ListenedTrack;
import org.ciakraa.wavelet.web_api.SpotifyActivityService;
import org.ciakraa.wavelet.web_api.SpotifyUnauthorizedException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisOperations;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public final class DefaultEventServiceUnitTest extends AbstractEventUnitTest {

    @Mock
    private SpotifyActivityService activityService;

    @Mock
    private RedisOperations<String, Object> redis;

    @Mock
    private BoundZSetOperations trackCache;

    private DefaultEventService target;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockRedis();

        target = new DefaultEventService(activityService, redis);
    }

    @Test
    public void shouldReturnEmptyListWhenNoRecentlyListenedTracks() throws SpotifyUnauthorizedException {
        when(activityService.getRecentlyListened(userCred, POLL_COUNT)).thenReturn(emptyList());

        List<ListenedTrack> tracks = target.getUniqueRecentlyListened(userCred, POLL_COUNT);
        assertThat(tracks).isEmpty();
    }

    @Test
    public void shouldReturnEmptyListWhenListenedTracksThrowSpotifyUnauthorizedException() throws SpotifyUnauthorizedException {
        when(activityService.getRecentlyListened(userCred, POLL_COUNT)).thenThrow(new SpotifyUnauthorizedException());

        List<ListenedTrack> tracks = target.getUniqueRecentlyListened(userCred, POLL_COUNT);
        assertThat(tracks).isEmpty();
    }

    @Test
    public void shouldReturnEmptyListWhenNoAudioFeatures() throws SpotifyUnauthorizedException {
        when(activityService.getRecentlyListened(userCred, POLL_COUNT)).thenReturn(getPlayHistories());
        when(activityService.getAudioFeatures(userCred, Arrays.asList(TRACK_ONE_ID, TRACK_TWO_ID))).thenReturn(emptyList());

        List<ListenedTrack> tracks = target.getUniqueRecentlyListened(userCred, POLL_COUNT);
        assertThat(tracks).isEmpty();
    }

    @Test
    public void shouldReturnEmptyListWhenAudioFeaturesThrowSpotifyUnauthorizedException() throws SpotifyUnauthorizedException {
        when(activityService.getRecentlyListened(userCred, POLL_COUNT)).thenReturn(getPlayHistories());
        when(activityService.getAudioFeatures(userCred, Arrays.asList(TRACK_ONE_ID, TRACK_TWO_ID))).thenThrow(new SpotifyUnauthorizedException());

        List<ListenedTrack> tracks = target.getUniqueRecentlyListened(userCred, POLL_COUNT);
        assertThat(tracks).isEmpty();
    }

    @Test
    public void shouldNotReturnAnyTracksFoundInCache() throws SpotifyUnauthorizedException {
        when(activityService.getRecentlyListened(userCred, POLL_COUNT)).thenReturn(getPlayHistories());
        when(activityService.getAudioFeatures(userCred, Arrays.asList(TRACK_ONE_ID, TRACK_TWO_ID))).thenReturn(getAudioFeatures());

        String trackOneKey =  DefaultEventService.getListenedTrackKey(getPlayHistories().get(0), userCred);
        String trackTwoKey =  DefaultEventService.getListenedTrackKey(getPlayHistories().get(1), userCred);
        when(trackCache.range(0, -1)).thenReturn(new HashSet<>(Arrays.asList(trackOneKey, trackTwoKey)));

        List<ListenedTrack> tracks = target.getUniqueRecentlyListened(userCred, POLL_COUNT);
        assertThat(tracks).isEmpty();
    }

    @Test
    public void shouldReturnAllTracks() throws SpotifyUnauthorizedException {
        when(activityService.getRecentlyListened(userCred, POLL_COUNT)).thenReturn(getPlayHistories());
        when(activityService.getAudioFeatures(userCred, Arrays.asList(TRACK_ONE_ID, TRACK_TWO_ID))).thenReturn(getAudioFeatures());
        when(trackCache.range(0, -1)).thenReturn(new HashSet<>());

        List<ListenedTrack> tracks = target.getUniqueRecentlyListened(userCred, POLL_COUNT);
        assertListenedTracks(tracks);
    }

    private void mockRedis() {
        when(redis.boundZSetOps(anyString())).thenReturn(trackCache);

        // Since we're not using the cache updates in the tests, just assume cache is always empty.
        when(trackCache.zCard()).thenReturn(0L);
    }
}
