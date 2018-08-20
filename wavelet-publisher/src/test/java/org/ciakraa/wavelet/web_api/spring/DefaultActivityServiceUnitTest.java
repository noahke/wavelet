package org.ciakraa.wavelet.web_api.spring;

import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.exceptions.detailed.UnauthorizedException;
import com.wrapper.spotify.model_objects.specification.AudioFeatures;
import com.wrapper.spotify.model_objects.specification.PlayHistory;
import org.ciakraa.wavelet.web_api.SpotifyUnauthorizedException;
import org.ciakraa.wavelet.web_api.SpotifyUserCredentials;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class DefaultActivityServiceUnitTest extends AbstractWebApiMockRequestUnitTest {

    private DefaultActivityService target;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        target = new DefaultActivityService(apiFactory);

        mockRequests();
    }

    @Test
    public void shouldThrowExceptionsWhenGetRecentlyListenedIsPassedInvalidArgs() {
        assertThatNullPointerException().isThrownBy(() -> target.getRecentlyListened(null, MAX_RECENTLY_LISTENED_TO));
        assertThatIllegalArgumentException().isThrownBy(() -> target.getRecentlyListened(mock(SpotifyUserCredentials.class), MAX_RECENTLY_LISTENED_TO));
        assertThatIllegalArgumentException().isThrownBy(() -> target.getRecentlyListened(getUserCred(), -1));
        assertThatIllegalArgumentException().isThrownBy(() -> target.getRecentlyListened(getUserCred(), MAX_RECENTLY_LISTENED_TO + 1));
        assertThatIllegalArgumentException().isThrownBy(() -> target.getRecentlyListened(getUserCred(), MAX_RECENTLY_LISTENED_TO, -1));
    }

    @Test
    public void shouldThrowExceptionWhenGetRecentlyListenedFailsOnUnauthorizedException() throws Exception {
        when(recentTracksReq.execute()).thenThrow(new UnauthorizedException());

        assertThatExceptionOfType(SpotifyUnauthorizedException.class)
                .isThrownBy(() -> target.getRecentlyListened(getUserCred(), MAX_RECENTLY_LISTENED_TO));
    }

    @Test
    public void shouldReturnEmptyCredWhenGetRecentlyListenedFailsWithSpotifyWebApiException() throws Exception  {
        when(recentTracksReq.execute()).thenThrow(new SpotifyWebApiException());

        List<PlayHistory> result = target.getRecentlyListened(getUserCred(), MAX_RECENTLY_LISTENED_TO);
        assertThat(result).isEmpty();
    }

    @Test
    public void shouldReturnTracksWhenGetRecentlyPlayedSucceeds() throws Exception  {
        mockHttpManager();

        List<PlayHistory> result = target.getRecentlyListened(getUserCred(), MAX_RECENTLY_LISTENED_TO);
        assertPlayHistories(result);

    }

    @Test
    public void shouldThrowExceptionsWhenGetFeaturedTracksIsPassedInvalidArgs() throws SpotifyUnauthorizedException {
        assertThatNullPointerException().isThrownBy(() -> target.getAudioFeatures(null, Arrays.asList(TRACK_ONE_ID, TRACK_TWO_ID)));
        assertThatIllegalArgumentException().isThrownBy(() -> target.getAudioFeatures(mock(SpotifyUserCredentials.class), Arrays.asList(TRACK_ONE_ID, TRACK_TWO_ID)));
        assertThatNullPointerException().isThrownBy(() -> target.getAudioFeatures(getUserCred(), null));
        assertThatIllegalArgumentException().isThrownBy(() -> target.getAudioFeatures(getUserCred(), emptyList()));
    }

    @Test
    public void shouldReturnEmptyCredWhenGetFeaturedTracksFailsWithSpotifyWebApiException() throws Exception  {
        when(audioFeaturesReq.execute()).thenThrow(new SpotifyWebApiException());

        List<AudioFeatures> result = target.getAudioFeatures(getUserCred(), Arrays.asList(TRACK_ONE_ID, TRACK_TWO_ID));
        assertThat(result).isEmpty();
    }

    @Test
    public void shouldReturnTracksWhenGetFeaturedTracksSucceeds() throws Exception  {
        mockHttpManager();

        List<AudioFeatures> result = target.getAudioFeatures(getUserCred(), Arrays.asList(TRACK_ONE_ID, TRACK_TWO_ID));

        assertAudioFeatures(result);
    }

    private void assertPlayHistories(List<PlayHistory> result) {
        PlayHistory trackOne = result.get(0);
        assertThat(trackOne.getTrack().getId()).isEqualTo(TRACK_ONE_ID);
        assertThat(trackOne.getPlayedAt()).isEqualTo(TRACK_ONE_PLAYED_AT);
        assertThat(trackOne.getTrack().getArtists()[0].getId()).isEqualTo(TRACK_ONE_ARTIST_ID);
        assertThat(trackOne.getTrack().getArtists()[0].getName()).isEqualTo(TRACK_ONE_ARTIST_NAME);
        assertThat(trackOne.getTrack().getDurationMs()).isEqualTo(TRACK_ONE_DURATION_MS);
        assertThat(trackOne.getTrack().getName()).isEqualTo(TRACK_ONE_NAME);
        assertThat(trackOne.getTrack().getPreviewUrl()).isEqualTo(TRACK_ONE_PREVIEW_URL);

        PlayHistory trackTwo = result.get(1);
        assertThat(trackTwo.getTrack().getId()).isEqualTo(TRACK_TWO_ID);
        assertThat(trackTwo.getPlayedAt()).isEqualTo(TRACK_TWO_PLAYED_AT);
        assertThat(trackTwo.getTrack().getArtists()[0].getId()).isEqualTo(TRACK_TWO_ARTIST_ID);
        assertThat(trackTwo.getTrack().getArtists()[0].getName()).isEqualTo(TRACK_TWO_ARTIST_NAME);
        assertThat(trackTwo.getTrack().getDurationMs()).isEqualTo(TRACK_TWO_DURATION_MS);
        assertThat(trackTwo.getTrack().getName()).isEqualTo(TRACK_TWO_NAME);
        assertThat(trackTwo.getTrack().getPreviewUrl()).isEqualTo(TRACK_TWO_PREVIEW_URL);
    }

    private void assertAudioFeatures(List<AudioFeatures> result) {
        AudioFeatures trackOne = result.get(0);
        assertThat(trackOne.getAcousticness()).isCloseTo(TRACK_ONE_ACOUSTICNESS, within(MARGIN_OF_ERROR));
        assertThat(trackOne.getDanceability()).isCloseTo(TRACK_ONE_DANCEABILITY, within(MARGIN_OF_ERROR));
        assertThat(trackOne.getEnergy()).isCloseTo(TRACK_ONE_ENERGY, within(MARGIN_OF_ERROR));
        assertThat(trackOne.getInstrumentalness()).isCloseTo(TRACK_ONE_INSTRUMENTALNESS, within(MARGIN_OF_ERROR));
        assertThat(trackOne.getKey()).isEqualTo(TRACK_ONE_KEY);
        assertThat(trackOne.getLiveness()).isCloseTo(TRACK_ONE_LIVENESS, within(MARGIN_OF_ERROR));
        assertThat(trackOne.getLoudness()).isCloseTo(TRACK_ONE_LOUDNESS, within(MARGIN_OF_ERROR));
        assertThat(trackOne.getMode().mode).isEqualTo(TRACK_ONE_MODE);
        assertThat(trackOne.getSpeechiness()).isCloseTo(TRACK_ONE_SPEECHINESS, within(MARGIN_OF_ERROR));
        assertThat(trackOne.getTempo()).isCloseTo(TRACK_ONE_TEMPO, within(MARGIN_OF_ERROR));
        assertThat(trackOne.getTimeSignature()).isEqualTo(TRACK_ONE_TIME_SIGNATURE);
        assertThat(trackOne.getValence()).isCloseTo(TRACK_ONE_VALENCE, within(MARGIN_OF_ERROR));

        AudioFeatures trackTwo = result.get(1);
        assertThat(trackTwo.getAcousticness()).isCloseTo(TRACK_TWO_ACOUSTICNESS, within(MARGIN_OF_ERROR));
        assertThat(trackTwo.getDanceability()).isCloseTo(TRACK_TWO_DANCEABILITY, within(MARGIN_OF_ERROR));
        assertThat(trackTwo.getEnergy()).isCloseTo(TRACK_TWO_ENERGY, within(MARGIN_OF_ERROR));
        assertThat(trackTwo.getInstrumentalness()).isCloseTo(TRACK_TWO_INSTRUMENTALNESS, within(MARGIN_OF_ERROR));
        assertThat(trackTwo.getKey()).isEqualTo(TRACK_TWO_KEY);
        assertThat(trackTwo.getLiveness()).isCloseTo(TRACK_TWO_LIVENESS, within(MARGIN_OF_ERROR));
        assertThat(trackTwo.getLoudness()).isCloseTo(TRACK_TWO_LOUDNESS, within(MARGIN_OF_ERROR));
        assertThat(trackTwo.getMode().mode).isEqualTo(TRACK_TWO_MODE);
        assertThat(trackTwo.getSpeechiness()).isCloseTo(TRACK_TWO_SPEECHINESS, within(MARGIN_OF_ERROR));
        assertThat(trackTwo.getTempo()).isCloseTo(TRACK_TWO_TEMPO, within(MARGIN_OF_ERROR));
        assertThat(trackTwo.getTimeSignature()).isEqualTo(TRACK_TWO_TIME_SIGNATURE);
        assertThat(trackTwo.getValence()).isCloseTo(TRACK_TWO_VALENCE, within(MARGIN_OF_ERROR));
    }
}
