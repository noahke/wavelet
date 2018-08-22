package org.ciakraa.wavelet.event;

import java.time.Duration;

final class EventConstants {

    private EventConstants() {
        // Static values class doesn't need to be instantiated!
    }

    /**
     * The max number of recently listened tracks that Spotify will return.
     */
    static final int MAX_RECENTLY_LISTENED_TO = 50;

    /**
     * We poll Spotify every 10 minutes for recently played tracks.
     */
    static final Duration POLL_PERIOD = Duration.ofMinutes(10);

    /**
     * Spotify counts a track as "listened" after 30s. So, our track count for polling is poll period * 2.
     */
    static final int POLL_COUNT = (int) POLL_PERIOD.toMinutes() * 2;

    /*
     * Kafka topic for listened tracks.
     */
    static final String KAFKA_TOPIC_LISTENED_TRACKS = "listenedTracks";

    /**
     * Prefix for a redis key: recently listened tracks by user.
     */
    static final String RECENTLY_LISTENED_KEY_PREFIX = "recently:";
}
