package org.ciakraa.wavelet.ui;

import org.ciakraa.wavelet.event.ListenedTrack;
import org.ciakraa.wavelet.event.ListenedTrackPollService;
import org.ciakraa.wavelet.web_api.SpotifyAuthorizationService;
import org.ciakraa.wavelet.web_api.SpotifyUnauthorizedException;
import org.ciakraa.wavelet.web_api.SpotifyUserCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;
import java.util.Optional;

/**
 * All user-facing web pages are managed by this controller.
 */
@Controller
@RequestMapping(value = "/wavelet")
final class SpringUiController {

    private final SpotifyAuthorizationService authService;
    private final URI redirectUri;
    private final String userScopes;
    private final ListenedTrackPollService pollService;

    @Autowired
    SpringUiController(SpotifyAuthorizationService authService, URI redirectUri, String userScopes,
                              ListenedTrackPollService pollService) {
        this.authService = authService;
        this.redirectUri = redirectUri;
        this.userScopes = userScopes;
        this.pollService = pollService;
    }

    /**
     * The home page welcomes the user to Wavelet and provides a link to Spotify so they can authorize this app.
     */
    @GetMapping(value = "/")
    public String home(Model model) {
        authService.requestAuthorizationUri(redirectUri, userScopes).ifPresent(authUri -> model.addAttribute("authUri", authUri));
        return "home";
    }

    /**
     * Upon authorizing the app, Spotify will redirect the user to this uri, providing a code and state value
     * which are used in creating {@link SpotifyUserCredentials}.
     *
     * We will then create a recurring poll of {@link ListenedTrack} for that user.
     *
     * @see <a href="https://developer.spotify.com/documentation/general/guides/authorization-guide/">Authorization Guide</a>
     */
    @GetMapping(value = "/authorize")
    public String authorize(@RequestParam String code, @RequestParam String state) {
        try {
            Optional<SpotifyUserCredentials> userCred = authService.authorizeUser(redirectUri, code, state);
            userCred.ifPresent(pollService::addPolls);

            return userCred.isPresent() ? "auth_success" : "auth_failure";
        } catch (SpotifyUnauthorizedException e) {
            return "auth_failure";
        }
    }
}
