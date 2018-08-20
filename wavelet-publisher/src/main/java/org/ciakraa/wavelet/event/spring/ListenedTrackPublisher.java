package org.ciakraa.wavelet.event.spring;

import org.apache.commons.lang3.Validate;
import org.ciakraa.wavelet.event.EventPublisher;
import org.ciakraa.wavelet.event.ListenedTrack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Each {@link ListenedTrack} will be published to a Kafka topic, keyed by userId.
 */
@Service
final class ListenedTrackPublisher implements EventPublisher<ListenedTrack>, EventConstants {

    private final KafkaTemplate<String, Object> kafka;

    @Autowired
    private ListenedTrackPublisher(KafkaTemplate<String, Object> kafka) {
        this.kafka = kafka;
    }

    @Override
    public void publish(Collection<ListenedTrack> events) {
        Validate.notEmpty(events);

        events.stream().forEach(this::publish);
    }

    private void publish(ListenedTrack track) {
        Validate.notNull(track);

        kafka.send(KAFKA_TOPIC_LISTENED_TRACKS, track);
    }
}
