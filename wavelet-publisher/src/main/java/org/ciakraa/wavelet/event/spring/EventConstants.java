package org.ciakraa.wavelet.event.spring;

import org.ciakraa.wavelet.web_api.SpotifyConstants;

import java.time.Duration;

public interface EventConstants extends SpotifyConstants {

    /**
     * We poll Spotify every 10 minutes for recently played tracks.
     */
    Duration POLL_PERIOD = Duration.ofMinutes(10);

    /**
     * Spotify counts a track as "listened" after 30s. So, our track count for polling is poll period * 2.
     */
    int POLL_COUNT = (int) POLL_PERIOD.toMinutes() * 2;

    /*
     * Kafka topic for listened tracks.
     */
    String KAFKA_TOPIC_LISTENED_TRACKS = "listenedTracks";

    /**
     * Prefix for a redis key: recently listened tracks by user.
     */
    String RECENTLY_LISTENED_KEY_PREFIX = "recently:";
}
