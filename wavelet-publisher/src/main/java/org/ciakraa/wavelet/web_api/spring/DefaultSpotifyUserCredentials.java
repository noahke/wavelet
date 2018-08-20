package org.ciakraa.wavelet.web_api.spring;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.apache.commons.lang3.Validate;
import org.ciakraa.wavelet.web_api.SpotifyClientCredentials;
import org.ciakraa.wavelet.web_api.SpotifyUserCredentials;

import java.util.Objects;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@JsonDeserialize(builder = DefaultSpotifyUserCredentials.Builder.class)
final class DefaultSpotifyUserCredentials implements SpotifyUserCredentials {

    private final String userId;
    private final String userDisplayName;
    private final String accessToken;
    private final String refreshToken;
    private final SpotifyClientCredentials clientCred;

    private DefaultSpotifyUserCredentials(final Builder builder) {
        this.userId = builder.userId;
        this.userDisplayName = builder.userDisplayName;
        this.accessToken = builder.accessToken;
        this.refreshToken = builder.refreshToken;
        this.clientCred = builder.clientCred;
    }

    @Override
    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public String getRefreshToken() {
        return refreshToken;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public String getUserDisplayName() {
        return userDisplayName;
    }

    @Override
    public SpotifyClientCredentials getClientCred() {
        return clientCred;
    }

    @JsonPOJOBuilder(withPrefix = "set")
    public static final class Builder {
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

        DefaultSpotifyUserCredentials build() {
            Validate.isTrue(clientCred.validate());
            Validate.notBlank(userId, accessToken, refreshToken);

            return new DefaultSpotifyUserCredentials(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultSpotifyUserCredentials that = (DefaultSpotifyUserCredentials) o;
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
