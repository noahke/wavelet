package org.ciakraa.wavelet.web_api;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;
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
public class SpotifyClientCredentials {

    @Id
    private final String clientSecret;
    private final String clientId;

    @JsonCreator
    SpotifyClientCredentials(@JsonProperty("clientId") String clientId, @JsonProperty("clientSecret") String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public boolean validate() {
        return (StringUtils.isNotEmpty(getClientId()) && StringUtils.isNotEmpty(getClientSecret()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpotifyClientCredentials that = (SpotifyClientCredentials) o;
        return Objects.equals(clientId, that.clientId) &&
                Objects.equals(clientSecret, that.clientSecret);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, clientSecret);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SpotifyClientCredentials{");
        sb.append("clientId='").append(clientId).append('\'');
        sb.append(", clientSecret='").append(clientSecret);
        sb.append('}');
        return sb.toString();
    }
}
