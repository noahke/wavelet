package org.ciakraa.wavelet.web_api.spring;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.ciakraa.wavelet.web_api.SpotifyClientCredentials;
import org.springframework.data.annotation.Id;

import java.util.Objects;

/**
 * Credentials necessary for authorizing requests to Spotify Web API.
 *
 * This application uses the Authorization Code Flow, which depends on
 * requesting an Access Token and Refresh Token.
 *
 * @see <a href="https://beta.developer.spotify.com/documentation/general/guides/authorization-guide/#authorization-code-flow">Authorization Code Flow</>
 */
final class DefaultSpotifyClientCredentials implements SpotifyClientCredentials {

    @Id
    private final String clientSecret;
    private final String clientId;

    @JsonCreator
    DefaultSpotifyClientCredentials(@JsonProperty("clientId") String clientId, @JsonProperty("clientSecret") String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    public String getClientSecret() {
        return clientSecret;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultSpotifyClientCredentials that = (DefaultSpotifyClientCredentials) o;
        return Objects.equals(clientId, that.clientId) &&
                Objects.equals(clientSecret, that.clientSecret);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, clientSecret);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DefaultSpotifyClientCredentials{");
        sb.append("clientId='").append(clientId).append('\'');
        sb.append(", clientSecret='").append(clientSecret);
        sb.append('}');
        return sb.toString();
    }
}
