package org.ciakraa.wavelet.web_api;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.util.Objects;

/**
 * Credentials necessary for authorizing app requests, involving user data, to Spotify Web API.
 *
 * This application involves the Authorization Code Flow, which depends on
 * requesting client credentials, as found in {@link SpotifyClientCredentials}.
 *
 * An object should be unique on its userId, client credentials, and refreshToken.
 *
 * @see <a href="https://beta.developer.spotify.com/documentation/general/guides/authorization-guide/#authorization-code-flow">Authorization Code Flow</>
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@JsonDeserialize(builder = SpotifyUserCredentials.Builder.class)
public class SpotifyUserCredentials {

    private final String userId;
    private final String userDisplayName;
    private final String accessToken;
    private final String refreshToken;
    private final SpotifyClientCredentials clientCred;

    private SpotifyUserCredentials(final Builder builder) {
        this.userId = builder.userId;
        this.userDisplayName = builder.userDisplayName;
        this.accessToken = builder.accessToken;
        this.refreshToken = builder.refreshToken;
        this.clientCred = builder.clientCred;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public SpotifyClientCredentials getClientCred() {
        return clientCred;
    }

    @JsonPOJOBuilder(withPrefix = "set")
    static final class Builder {
        private String userId;
        private String userDisplayName;
        private String accessToken;
        private String refreshToken;
        private SpotifyClientCredentials clientCred;

        Builder setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        Builder setUserDisplayName(String userDisplayName) {
            this.userDisplayName = userDisplayName;
            return this;
        }

        Builder setAccessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        Builder setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        Builder setClientCred(SpotifyClientCredentials clientCred) {
            this.clientCred = clientCred;
            return this;
        }

        SpotifyUserCredentials build() {
            Validate.isTrue(clientCred.validate());
            Validate.notBlank(userId, accessToken, refreshToken);

            return new SpotifyUserCredentials(this);
        }
    }

    public boolean validate() {
        return StringUtils.isNotBlank(getUserId()) &&
                StringUtils.isNotBlank(getAccessToken()) &&
                StringUtils.isNotBlank(getRefreshToken());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpotifyUserCredentials that = (SpotifyUserCredentials) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(refreshToken, that.refreshToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, refreshToken);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SpotifyUserCredentials{");
        sb.append("userId='").append(userId).append('\'');
        sb.append(", userName='").append(userDisplayName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
