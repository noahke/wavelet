package org.ciakraa.wavelet.event.spring;

import org.ciakraa.wavelet.event.ListenedTrack;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;


/**
 * Make sure kafka is running for this test!
 */
public final class ListenedTrackPublisherIntegrationTest extends AbstractEventIntegrationTest {

    private static final Logger LOG = LoggerFactory.getLogger(ListenedTrackPublisherIntegrationTest.class);

    private CountDownLatch lock = new CountDownLatch(2);
    private AtomicInteger trackCount = new AtomicInteger(0);

    @Autowired
    private ListenedTrackPublisher publisher;

    @Autowired
    private KafkaListenerEndpointRegistry registry;

    @Test
    public void shouldSuccessfullyPublishTracks() throws InterruptedException {
        publisher.publish(getListenedTracks());
        lock.await(1000, TimeUnit.MILLISECONDS);
        assertThat(trackCount.get()).isEqualTo(2);
    }

    @KafkaListener(topics = KAFKA_TOPIC_LISTENED_TRACKS)
    public void listenForTracks(ListenedTrack track) {
        LOG.info("Received listened track: {}", track.getTrackId());
        if (track == null) {
            fail("Published track is null");
        }
        if (track.getTrackId().equals(TRACK_ONE_ID)) {
            assertListenedTrackOne(track);
            trackCount.incrementAndGet();
            lock.countDown();
            return;
        }
        if (track.getTrackId().equals(TRACK_TWO_ID)) {
            assertListenedTrackTwo(track);
            trackCount.incrementAndGet();
            lock.countDown();
            return;
        }
        fail("Published track is invalid");
    }
}
