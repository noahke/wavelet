package org.ciakraa.wavelet.ui;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
class SpringUiConfig {

    @Value("${spotify.redirectUri}")
    private URI redirectUri;

    @Value("${spotify.userScopes}")
    private String userScopes;

    /**
     * RedirectURI is the page that Spotify will return a user to, once they've authorized this app.
     */
    @Bean
    URI redirectUri() {
        return redirectUri;
    }

    /**
     * User scopes are a Spotify Web API param that indicate which data points are open to request for this app.
     * @see <a href="https://developer.spotify.com/documentation/general/guides/scopes/">Authorization Scopes</a>
     */
    @Bean
    String userScopes() {
        return userScopes;
    }
}
