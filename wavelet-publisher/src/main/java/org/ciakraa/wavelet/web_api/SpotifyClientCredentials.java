package org.ciakraa.wavelet.web_api;

import org.apache.commons.lang3.StringUtils;

/**
 * Credentials necessary for authorizing app requests to Spotify Web API.
 *
 * This application involves the Authorization Code Flow, which depends on
 * requesting an Access Token and Refresh Token, as found in {@link SpotifyUserCredentials}.
 *
 * Objects should be unique on their clientID and client secret.
 *
 * @see <a href="https://beta.developer.spotify.com/documentation/general/guides/authorization-guide/#authorization-code-flow">Authorization Code Flow</>
 */
public interface SpotifyClientCredentials {

    String getClientId();

    String getClientSecret();

    default boolean validate() {
        return (StringUtils.isNotEmpty(getClientId()) && StringUtils.isNotEmpty(getClientSecret()));
    }
}
