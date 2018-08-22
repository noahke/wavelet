package org.ciakraa.wavelet.common;

import com.wrapper.spotify.SpotifyHttpManager;

import java.net.URI;

/**
 * Static test values all drawn from the examples and test json found in Spotify Web API Java library.
 *
 * @see <a href="https://github.com/thelinmichael/spotify-web-api-java/tree/master/examples">Spotify Web API Java: Examples</a>
 * @see <a href="https://github.com/thelinmichael/spotify-web-api-java/tree/master/src/test/fixtures/requests"Spotify Web API Java: Test JSON></a>
 */
public class CommonTestConstants {

    private CommonTestConstants() {
        // Static values class doesn't need to be instantiated!
    }

    /**
     * Authorization related Constants.
     */
    public static final String CLIENT_ID = "zyuxhfo1c51b5hxjk09x2uhv5n0svgd6g";

    public static final String CLIENT_SECRET = "zudknyqbh3wunbhcvg9uyvo7uwzeu6nne";

    public static final URI REDIRECT_URI = SpotifyHttpManager.makeUri("https://example.com/spotify-redirect");

    public static final String SCOPES = "user-read-birthdate,user-read-email";

    public static final String STATE = "STATE";

    public static final String AUTHORIZATION_CODE = "c-oGaPdYJF3tu3oUZRUiBHWQvm4oHnBrsxfHackYzzomKJiy5te1k04LJdr6XxjAC" +
            "e9TonpJR8NPOQ3o5btASx_oMw4trmXLYdkda77wY0NJ9Scl69lKvGiOfdnRi5Q0IbBu185Y0TZgyUJz3Auqqv-Wk7zjRke4Dzq" +
            "YEc3ucyUBOq08j5223te-G2K72aL9PxgVJaEHBbLvhdJscCy-zcyU29EZoNlG_E5";

    public static final String ACCESS_TOKEN = "taHZ2SdB-bPA3FsK3D7ZN5npZS47cMy-IEySVEGttOhXmqaVAIo0ESvTCLjLBifhHOHOIuh" +
            "FUKPW1WMDP7w6dj3MAZdWT8CLI2MkZaXbYLTeoDvXesf2eeiLYPBGdx8tIwQJKgV8XdnzH_DONk";

    // This is the access token returned after a refresh request.
    public static final String REFRESHED_ACCESS_TOKEN = "aHZ2SdB-bPA3FsK3D7ZN5npZS47cMy-IEySVEGttOhXmqaVAIo0ESvTCLjLBi" +
            "fhHOHOIuhFUKPW1WMDP7w6dj3MAZdWT8CLI2MkZaXbYLTeoDvXesf2eeiLYPBGdx8tIwQJKgV8XdnzH_DONk";

    public static final String REFRESH_TOKEN = "b0KuPuLw77Z0hQhCsK-GTHoEx_kethtn357V7iqwEpCTIsLgqbBC_vQBTGC6M5rINl0Frq" +
            "HK-D3cbOsMOlfyVKuQPvpyGcLcxAoLOTpYXc28nVwB7iBq2oKj9G9lHkFOUKn";

    /**
     * User constants.
     */
    public static final String USER_ID = "wizzler";

    public static final String USER_DISPLAY_NAME = "JM Wizzler";

    /**
     * Recently Played track constants.
     */
    public static final String TRACK_ONE_ID = "2gNfxysfBRfl9Lvi9T3v6R";
    // Note: This is different than the Json, which is in GMT. See SpringSpotifyEventService for details.
    public static final String TRACK_ONE_PLAYED_AT = "2016-12-13T15:44:04.000Z";
    public static final long TRACK_ONE_PLAYED_AT_TIMESTAMP = 1481661840L;
    public static final String TRACK_ONE_ARTIST_ID = "5INjqkS1o8h1imAzPqGZBb";
    public static final String TRACK_ONE_ARTIST_NAME = "Tame Impala";
    public static final int TRACK_ONE_DURATION_MS = 108546;
    public static final String TRACK_ONE_NAME = "Disciples";
    public static final String TRACK_ONE_PREVIEW_URL = "https://p.scdn.co/mp3-preview/6023e5aac2123d098ce490488966b28838b14fa2";

    public static final String TRACK_TWO_ID = "2X485T9Z5Ly0xyaghN73ed";
    // Note: This is different than the Json, which is in GMT. See SpringSpotifyEventService for details.
    public static final String TRACK_TWO_PLAYED_AT = "2016-12-13T15:42:17.000Z";
    public static final long TRACK_TWO_PLAYED_AT_TIMESTAMP = 1481661720L;
    public static final String TRACK_TWO_ARTIST_ID = "5INjqkS1o8h1imAzPqGZBb";
    public static final String TRACK_TWO_ARTIST_NAME = "Tame Impala";
    public static final int TRACK_TWO_DURATION_MS = 467586;
    public static final String TRACK_TWO_NAME = "Let It Happen";
    public static final String TRACK_TWO_PREVIEW_URL = "https://p.scdn.co/mp3-preview/05dee1ad0d2a6fa4ad07fbd24ae49d58468e8194";

    /**
     * Audio Features track constants.
     */
    public static final float TRACK_ONE_DANCEABILITY = 0.808f;
    public static final float TRACK_ONE_ENERGY = 0.626f;
    public static final int TRACK_ONE_KEY = 7;
    public static final float TRACK_ONE_LOUDNESS = -12.733f;
    public static final int TRACK_ONE_MODE = 1;
    public static final float TRACK_ONE_SPEECHINESS = 0.168f;
    public static final float TRACK_ONE_ACOUSTICNESS = 0.00187f;
    public static final float TRACK_ONE_INSTRUMENTALNESS = 0.159f;
    public static final float TRACK_ONE_LIVENESS = 0.376f;
    public static final float TRACK_ONE_VALENCE = 0.369f;
    public static final float TRACK_ONE_TEMPO = 123.99f;
    public static final int TRACK_ONE_TIME_SIGNATURE = 4;

    public static final float TRACK_TWO_DANCEABILITY = 0.457f;
    public static final float TRACK_TWO_ENERGY = 0.815f;
    public static final int TRACK_TWO_KEY = 1;
    public static final float TRACK_TWO_LOUDNESS = -7.199f;
    public static final int TRACK_TWO_MODE = 1;
    public static final float TRACK_TWO_SPEECHINESS = 0.034f;
    public static final float TRACK_TWO_ACOUSTICNESS = 0.102f;
    public static final float TRACK_TWO_INSTRUMENTALNESS = 0.0319f;
    public static final float TRACK_TWO_LIVENESS = 0.103f;
    public static final float TRACK_TWO_VALENCE = 0.382f;
    public static final float TRACK_TWO_TEMPO = 96.083f;
    public static final int TRACK_TWO_TIME_SIGNATURE = 4;

    // Since we're getting doubles from JSON, the ultimate values in java may not match exactly.
    public static final float MARGIN_OF_ERROR = 0.00001f;

    /**
     * File constants. File Names are appended to the JSON_DIR.
     */
    public static final String JSON_DIR = "src/test/resources/json";

    public static final String AUTHORIZATION_CODE_JSON = "AuthorizationCode.json";

    public static final String AUTHORIZATION_REFRESH_JSON = "AuthorizationCodeRefresh.json";

    public static final String USER_PROFILE_JSON = "UserProfile.json";

    public static final String RECENTLY_PLAYED_JSON = "RecentlyPlayedTracks.json";

    public static final String AUDIO_FEATURES = "AudioFeatures.json";

}
