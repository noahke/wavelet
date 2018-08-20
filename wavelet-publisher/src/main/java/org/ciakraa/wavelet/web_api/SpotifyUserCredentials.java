package org.ciakraa.wavelet.web_api;

import org.apache.commons.lang3.StringUtils;

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
public interface SpotifyUserCredentials {

    String getUserId();

    String getUserDisplayName();

    String getAccessToken();

    String getRefreshToken();

    SpotifyClientCredentials getClientCred();

    default boolean validate() {
        return StringUtils.isNotBlank(getUserId()) &&
                StringUtils.isNotBlank(getAccessToken()) &&
                StringUtils.isNotBlank(getRefreshToken());
    }

}
