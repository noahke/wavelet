package org.ciakraa.wavelet.event;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Each {@link ListenedTrack} will be published to a Kafka topic, keyed by userId.
 */
@Service
final class ListenedTrackPublisher implements UserEventPublisher<ListenedTrack> {

    private final KafkaTemplate<String, Object> kafka;

    @Autowired
    ListenedTrackPublisher(KafkaTemplate<String, Object> kafka) {
        this.kafka = kafka;
    }

    @Override
    public void publish(Collection<ListenedTrack> events) {
        Validate.notEmpty(events);

        events.stream().forEach(this::publish);
    }

    private void publish(ListenedTrack track) {
        Validate.notNull(track);

        kafka.send(EventConstants.KAFKA_TOPIC_LISTENED_TRACKS, track);
    }
}
