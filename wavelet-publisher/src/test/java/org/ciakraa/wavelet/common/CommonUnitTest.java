package org.ciakraa.wavelet.common;

import com.wrapper.spotify.enums.Modality;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.AudioFeatures;
import com.wrapper.spotify.model_objects.specification.PlayHistory;
import com.wrapper.spotify.model_objects.specification.TrackSimplified;
import org.ciakraa.wavelet.event.ListenedTrack;
import org.ciakraa.wavelet.web_api.SpotifyUserCredentials;
import org.mockito.Mock;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * Simple abstract class to provide common functionality to unit tests.
 *
 * Test data and a few util methods courtesy of the Spotify Web API Java library.
 * @see <a href="https://github.com/thelinmichael/spotify-web-api-java/tree/master/examples">Spotify Web API Java: Examples</a>
 */
public abstract class CommonUnitTest implements CommonTestConstants {

    @Mock
    protected SpotifyUserCredentials userCred;

    protected List<PlayHistory> getPlayHistories() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date trackOnePlayedAt = null;
        Date trackTwoPlayedAt = null;
        try {
            trackOnePlayedAt = dateFormat.parse(TRACK_ONE_PLAYED_AT);
            trackTwoPlayedAt = dateFormat.parse(TRACK_TWO_PLAYED_AT);
        } catch (ParseException e) {
            fail("Parse exception for tracks.");
        }

        ArtistSimplified artistOne = new ArtistSimplified.Builder()
                .setId(TRACK_ONE_ARTIST_ID)
                .setName(TRACK_ONE_ARTIST_NAME)
                .build();

        TrackSimplified trackOne = new TrackSimplified.Builder()
                .setId(TRACK_ONE_ID)
                .setArtists(artistOne)
                .setDurationMs(TRACK_ONE_DURATION_MS)
                .setName(TRACK_ONE_NAME)
                .setPreviewUrl(TRACK_ONE_PREVIEW_URL)
                .build();

        PlayHistory playOne = new PlayHistory.Builder()
                .setTrack(trackOne)
                .setPlayedAt(trackOnePlayedAt)
                .build();

        ArtistSimplified artistTwo = new ArtistSimplified.Builder()
                .setId(TRACK_TWO_ARTIST_ID)
                .setName(TRACK_TWO_ARTIST_NAME)
                .build();

        TrackSimplified trackTwo = new TrackSimplified.Builder()
                .setId(TRACK_TWO_ID)
                .setArtists(artistTwo)
                .setDurationMs(TRACK_TWO_DURATION_MS)
                .setName(TRACK_TWO_NAME)
                .setPreviewUrl(TRACK_TWO_PREVIEW_URL)
                .build();

        PlayHistory playTwo = new PlayHistory.Builder()
                .setTrack(trackTwo)
                .setPlayedAt(trackTwoPlayedAt)
                .build();

        return Arrays.asList(playOne, playTwo);
    }

    protected List<AudioFeatures> getAudioFeatures() {
        AudioFeatures featuresOne = new AudioFeatures.Builder()
                .setId(TRACK_ONE_ID)
                .setAcousticness(TRACK_ONE_ACOUSTICNESS)
                .setDanceability(TRACK_ONE_DANCEABILITY)
                .setEnergy(TRACK_ONE_ENERGY)
                .setInstrumentalness(TRACK_ONE_INSTRUMENTALNESS)
                .setKey(TRACK_ONE_KEY)
                .setLiveness(TRACK_ONE_LIVENESS)
                .setLoudness(TRACK_ONE_LOUDNESS)
                .setMode(Modality.MAJOR)
                .setSpeechiness(TRACK_ONE_SPEECHINESS)
                .setTempo(TRACK_ONE_TEMPO)
                .setTimeSignature(TRACK_ONE_TIME_SIGNATURE)
                .setValence(TRACK_ONE_VALENCE)
                .build();


        AudioFeatures featuresTwo = new AudioFeatures.Builder()
                .setId(TRACK_TWO_ID)
                .setAcousticness(TRACK_TWO_ACOUSTICNESS)
                .setDanceability(TRACK_TWO_DANCEABILITY)
                .setEnergy(TRACK_TWO_ENERGY)
                .setInstrumentalness(TRACK_TWO_INSTRUMENTALNESS)
                .setKey(TRACK_TWO_KEY)
                .setLiveness(TRACK_TWO_LIVENESS)
                .setLoudness(TRACK_TWO_LOUDNESS)
                .setMode(Modality.MAJOR)
                .setSpeechiness(TRACK_TWO_SPEECHINESS)
                .setTempo(TRACK_TWO_TEMPO)
                .setTimeSignature(TRACK_TWO_TIME_SIGNATURE)
                .setValence(TRACK_TWO_VALENCE)
                .build();

        return Arrays.asList(featuresOne, featuresTwo);
    }

    protected List<ListenedTrack> getListenedTracks() {
        ListenedTrack trackOne = new ListenedTrack();
        trackOne.setTrackId(TRACK_ONE_ID);
        trackOne.setPlayedAt(TRACK_ONE_PLAYED_AT_TIMESTAMP);
        trackOne.setArtistId(TRACK_ONE_ARTIST_ID);
        trackOne.setArtistName(TRACK_ONE_ARTIST_NAME);
        trackOne.setDurationMs(TRACK_ONE_DURATION_MS);
        trackOne.setName(TRACK_ONE_NAME);
        trackOne.setPreviewUrl(TRACK_ONE_PREVIEW_URL);
        trackOne.setAcousticness(TRACK_ONE_ACOUSTICNESS);
        trackOne.setDanceability(TRACK_ONE_DANCEABILITY);
        trackOne.setEnergy(TRACK_ONE_ENERGY);
        trackOne.setInstrumentalness(TRACK_ONE_INSTRUMENTALNESS);
        trackOne.setKeySignature(TRACK_ONE_KEY);
        trackOne.setLiveness(TRACK_ONE_LIVENESS);
        trackOne.setLoudness(TRACK_ONE_LOUDNESS);
        trackOne.setMode(TRACK_ONE_MODE);
        trackOne.setSpeechiness(TRACK_ONE_SPEECHINESS);
        trackOne.setTempo(TRACK_ONE_TEMPO);
        trackOne.setTimeSignature(TRACK_ONE_TIME_SIGNATURE);
        trackOne.setValence(TRACK_ONE_VALENCE);

        ListenedTrack trackTwo = new ListenedTrack();
        trackTwo.setTrackId(TRACK_TWO_ID);
        trackTwo.setPlayedAt(TRACK_TWO_PLAYED_AT_TIMESTAMP);
        trackTwo.setArtistId(TRACK_TWO_ARTIST_ID);
        trackTwo.setArtistName(TRACK_TWO_ARTIST_NAME);
        trackTwo.setDurationMs(TRACK_TWO_DURATION_MS);
        trackTwo.setName(TRACK_TWO_NAME);
        trackTwo.setPreviewUrl(TRACK_TWO_PREVIEW_URL);
        trackTwo.setAcousticness(TRACK_TWO_ACOUSTICNESS);
        trackTwo.setDanceability(TRACK_TWO_DANCEABILITY);
        trackTwo.setEnergy(TRACK_TWO_ENERGY);
        trackTwo.setInstrumentalness(TRACK_TWO_INSTRUMENTALNESS);
        trackTwo.setKeySignature(TRACK_TWO_KEY);
        trackTwo.setLiveness(TRACK_TWO_LIVENESS);
        trackTwo.setLoudness(TRACK_TWO_LOUDNESS);
        trackTwo.setMode(TRACK_TWO_MODE);
        trackTwo.setSpeechiness(TRACK_TWO_SPEECHINESS);
        trackTwo.setTempo(TRACK_TWO_TEMPO);
        trackTwo.setTimeSignature(TRACK_TWO_TIME_SIGNATURE);
        trackTwo.setValence(TRACK_TWO_VALENCE);

        return Arrays.asList(trackOne, trackTwo);
    }

    protected void assertListenedTracks(List<ListenedTrack> result) {
        assertListenedTrackOne(result.get(0));
        assertListenedTrackTwo(result.get(1));
    }

    protected void assertListenedTrackOne(ListenedTrack trackOne) {
        assertThat(trackOne.getTrackId()).isEqualTo(TRACK_ONE_ID);
        assertThat(trackOne.getPlayedAt()).isEqualTo(TRACK_ONE_PLAYED_AT_TIMESTAMP);
        assertThat(trackOne.getArtistId()).isEqualTo(TRACK_ONE_ARTIST_ID);
        assertThat(trackOne.getArtistName()).isEqualTo(TRACK_ONE_ARTIST_NAME);
        assertThat(trackOne.getDurationMs()).isEqualTo(TRACK_ONE_DURATION_MS);
        assertThat(trackOne.getName()).isEqualTo(TRACK_ONE_NAME);
        assertThat(trackOne.getPreviewUrl()).isEqualTo(TRACK_ONE_PREVIEW_URL);
        assertThat(trackOne.getAcousticness()).isEqualTo(TRACK_ONE_ACOUSTICNESS);
        assertThat(trackOne.getDanceability()).isEqualTo(TRACK_ONE_DANCEABILITY);
        assertThat(trackOne.getEnergy()).isEqualTo(TRACK_ONE_ENERGY);
        assertThat(trackOne.getInstrumentalness()).isEqualTo(TRACK_ONE_INSTRUMENTALNESS);
        assertThat(trackOne.getKeySignature()).isEqualTo(TRACK_ONE_KEY);
        assertThat(trackOne.getLiveness()).isEqualTo(TRACK_ONE_LIVENESS);
        assertThat(trackOne.getLoudness()).isEqualTo(TRACK_ONE_LOUDNESS);
        assertThat(trackOne.getMode()).isEqualTo(TRACK_ONE_MODE);
        assertThat(trackOne.getSpeechiness()).isEqualTo(TRACK_ONE_SPEECHINESS);
        assertThat(trackOne.getTempo()).isEqualTo(TRACK_ONE_TEMPO);
        assertThat(trackOne.getTimeSignature()).isEqualTo(TRACK_ONE_TIME_SIGNATURE);
        assertThat(trackOne.getValence()).isEqualTo(TRACK_ONE_VALENCE);
    }

    protected void assertListenedTrackTwo(ListenedTrack trackTwo) {
        assertThat(trackTwo.getTrackId()).isEqualTo(TRACK_TWO_ID);
        assertThat(trackTwo.getPlayedAt()).isEqualTo(TRACK_TWO_PLAYED_AT_TIMESTAMP);
        assertThat(trackTwo.getArtistId()).isEqualTo(TRACK_TWO_ARTIST_ID);
        assertThat(trackTwo.getArtistName()).isEqualTo(TRACK_TWO_ARTIST_NAME);
        assertThat(trackTwo.getDurationMs()).isEqualTo(TRACK_TWO_DURATION_MS);
        assertThat(trackTwo.getName()).isEqualTo(TRACK_TWO_NAME);
        assertThat(trackTwo.getPreviewUrl()).isEqualTo(TRACK_TWO_PREVIEW_URL);
        assertThat(trackTwo.getAcousticness()).isEqualTo(TRACK_TWO_ACOUSTICNESS);
        assertThat(trackTwo.getDanceability()).isEqualTo(TRACK_TWO_DANCEABILITY);
        assertThat(trackTwo.getEnergy()).isEqualTo(TRACK_TWO_ENERGY);
        assertThat(trackTwo.getInstrumentalness()).isEqualTo(TRACK_TWO_INSTRUMENTALNESS);
        assertThat(trackTwo.getKeySignature()).isEqualTo(TRACK_TWO_KEY);
        assertThat(trackTwo.getLiveness()).isEqualTo(TRACK_TWO_LIVENESS);
        assertThat(trackTwo.getLoudness()).isEqualTo(TRACK_TWO_LOUDNESS);
        assertThat(trackTwo.getMode()).isEqualTo(TRACK_TWO_MODE);
        assertThat(trackTwo.getSpeechiness()).isEqualTo(TRACK_TWO_SPEECHINESS);
        assertThat(trackTwo.getTempo()).isEqualTo(TRACK_TWO_TEMPO);
        assertThat(trackTwo.getTimeSignature()).isEqualTo(TRACK_TWO_TIME_SIGNATURE);
        assertThat(trackTwo.getValence()).isEqualTo(TRACK_TWO_VALENCE);
    }
}
