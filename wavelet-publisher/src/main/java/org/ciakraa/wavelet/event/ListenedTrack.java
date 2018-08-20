package org.ciakraa.wavelet.event;

/**
 * A {@UserEvent} where a Spotify Track has been listened to.
 *
 * Fields correspond to, but do not fully replicate, that of a Play History object
 * returned by Spotify's Web Api combined with an AudioFeatures object. Only fields necessary for this hobby project
 * were included ;)
 *
 * Satisfies POJO criteria (documented in {@UserEvent}) to be eligible for Kafka + Flink processing.
 *
 * @see <a href="https://beta.developer.spotify.com/documentation/web-api/reference/player/get-recently-played/">Spotify Web API: Recently Played</a>
 */
public class ListenedTrack extends UserEvent {

    // Key is userId + trackId + timestamp, e.g. "user-100-1528060487"
    private String key;

    // Simple Track data points
    private String trackId;
    private long playedAt;
    private String artistName;
    private String artistId;
    private int durationMs;
    private String name;
    private String previewUrl;

    // Audio Features
    private double acousticness;
    private double danceability;
    private double energy;
    private double instrumentalness;
    private int keySignature;
    private double liveness;
    private double loudness;
    private int mode;
    private double speechiness;
    private double tempo;
    private int timeSignature;
    private double valence;

    public ListenedTrack() {}

    public final String getKey() {
        return key;
    }

    public final void setKey(String key) {
        this.key = key;
    }

    public final String getTrackId() {
        return trackId;
    }

    public final void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public final long getPlayedAt() {
        return playedAt;
    }

    public final void setPlayedAt(long playedAt) {
        this.playedAt = playedAt;
    }

    public final String getArtistName() {
        return artistName;
    }

    public final void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public final String getArtistId() {
        return artistId;
    }

    public final void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public final int getDurationMs() {
        return durationMs;
    }

    public final void setDurationMs(int durationMs) {
        this.durationMs = durationMs;
    }

    public final String getName() {
        return name;
    }

    public final void setName(String name) {
        this.name = name;
    }

    public final String getPreviewUrl() {
        return previewUrl;
    }

    public final void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public final double getAcousticness() {
        return acousticness;
    }

    public final void setAcousticness(double acousticness) {
        this.acousticness = acousticness;
    }

    public final double getDanceability() {
        return danceability;
    }

    public final void setDanceability(double danceability) {
        this.danceability = danceability;
    }

    public final double getEnergy() {
        return energy;
    }

    public final void setEnergy(double energy) {
        this.energy = energy;
    }

    public final double getInstrumentalness() {
        return instrumentalness;
    }

    public final void setInstrumentalness(double instrumentalness) {
        this.instrumentalness = instrumentalness;
    }

    public final int getKeySignature() {
        return keySignature;
    }

    public final void setKeySignature(int keySignature) {
        this.keySignature = keySignature;
    }

    public final double getLiveness() {
        return liveness;
    }

    public final void setLiveness(double liveness) {
        this.liveness = liveness;
    }

    public final double getLoudness() {
        return loudness;
    }

    public final void setLoudness(double loudness) {
        this.loudness = loudness;
    }

    public final int getMode() {
        return mode;
    }

    public final void setMode(int mode) {
        this.mode = mode;
    }

    public final double getSpeechiness() {
        return speechiness;
    }

    public final void setSpeechiness(double speechiness) {
        this.speechiness = speechiness;
    }

    public final double getTempo() {
        return tempo;
    }

    public final void setTempo(double tempo) {
        this.tempo = tempo;
    }

    public final int getTimeSignature() {
        return timeSignature;
    }

    public final void setTimeSignature(int timeSignature) {
        this.timeSignature = timeSignature;
    }

    public final double getValence() {
        return valence;
    }

    public final void setValence(double valence) {
        this.valence = valence;
    }

}
