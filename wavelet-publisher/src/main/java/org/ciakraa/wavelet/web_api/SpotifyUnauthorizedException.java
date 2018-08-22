package org.ciakraa.wavelet.web_api;

import org.apache.http.HttpException;

/**
 * If Spotify responds to a request with a 401 status code, then the request is unauthorized,
 * and you should probably refresh your access token. Or give up entirely!!
 *
 * @see <a href="https://beta.developer.spotify.com/documentation/web-api/#response-status-codes">Spotify API: Response Codes</a>
 */
public class SpotifyUnauthorizedException extends HttpException {

    public SpotifyUnauthorizedException() {
        super();
    }

    public SpotifyUnauthorizedException(String message) {
        super(message);
    }

    public SpotifyUnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }


}
