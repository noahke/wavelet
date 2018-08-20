package org.ciakraa.wavelet.event;

import java.util.Collection;

/**
 * Service implementations will publish each {@link UserEvent} to a Kafka topic.
 *
 * @param <T>
 */
public interface EventPublisher<T extends UserEvent> {

    void publish(Collection<T> events);
}
