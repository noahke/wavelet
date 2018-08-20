package org.ciakraa.wavelet;

import org.ciakraa.wavelet.event.ListenedTrack;
import org.ciakraa.wavelet.event.PollService;
import org.ciakraa.wavelet.web_api.SpotifyUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("prod")
@Component
public class Runner implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(Runner.class);

    private final SpotifyUserService userService;
    private final PollService<ListenedTrack> listenedTrackPollService;

    @Autowired
    public Runner(SpotifyUserService userService, PollService<ListenedTrack> listenedTrackPollService) {
        this.userService = userService;
        this.listenedTrackPollService = listenedTrackPollService;
    }

    @Override
    public void run(String... args) throws Exception {
        LOG.info("Initializing event polls for saved users.");
        userService.findAll().stream().forEach(listenedTrackPollService::addPolls);
    }
}
