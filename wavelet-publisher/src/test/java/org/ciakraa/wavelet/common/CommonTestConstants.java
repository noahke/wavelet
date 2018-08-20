package org.ciakraa.wavelet.common;

import com.wrapper.spotify.SpotifyHttpManager;

import java.net.URI;

/**
 * Static test values all drawn from the examples and test json found in Spotify Web API Java library.
 *
 * @see <a href="https://github.com/thelinmichael/spotify-web-api-java/tree/master/examples">Spotify Web API Java: Examples</a>
 * @see <a href="https://github.com/thelinmichael/spotify-web-api-java/tree/master/src/test/fixtures/requests"Spotify Web API Java: Test JSON></a>
 */
public interface CommonTestConstants {

    /**
     * Authorization related Constants.
     */
    String CLIENT_ID = "zyuxhfo1c51b5hxjk09x2uhv5n0svgd6g";

    String CLIENT_SECRET = "zudknyqbh3wunbhcvg9uyvo7uwzeu6nne";

    URI REDIRECT_URI = SpotifyHttpManager.makeUri("https://example.com/spotify-redirect");

    String SCOPES = "user-read-birthdate,user-read-email";

    String STATE = "STATE";

    String AUTHORIZATION_CODE = "c-oGaPdYJF3tu3oUZRUiBHWQvm4oHnBrsxfHackYzzomKJiy5te1k04LJdr6XxjAC" +
            "e9TonpJR8NPOQ3o5btASx_oMw4trmXLYdkda77wY0NJ9Scl69lKvGiOfdnRi5Q0IbBu185Y0TZgyUJz3Auqqv-Wk7zjRke4Dzq" +
            "YEc3ucyUBOq08j5223te-G2K72aL9PxgVJaEHBbLvhdJscCy-zcyU29EZoNlG_E5";

    String ACCESS_TOKEN = "taHZ2SdB-bPA3FsK3D7ZN5npZS47cMy-IEySVEGttOhXmqaVAIo0ESvTCLjLBifhHOHOIuh" +
            "FUKPW1WMDP7w6dj3MAZdWT8CLI2MkZaXbYLTeoDvXesf2eeiLYPBGdx8tIwQJKgV8XdnzH_DONk";

    // This is the access token returned after a refresh request.
    String REFRESHED_ACCESS_TOKEN = "aHZ2SdB-bPA3FsK3D7ZN5npZS47cMy-IEySVEGttOhXmqaVAIo0ESvTCLjLBi" +
            "fhHOHOIuhFUKPW1WMDP7w6dj3MAZdWT8CLI2MkZaXbYLTeoDvXesf2eeiLYPBGdx8tIwQJKgV8XdnzH_DONk";

    String REFRESH_TOKEN = "b0KuPuLw77Z0hQhCsK-GTHoEx_kethtn357V7iqwEpCTIsLgqbBC_vQBTGC6M5rINl0Frq" +
            "HK-D3cbOsMOlfyVKuQPvpyGcLcxAoLOTpYXc28nVwB7iBq2oKj9G9lHkFOUKn";

    /**
     * User constants.
     */
    String USER_ID = "wizzler";

    String USER_DISPLAY_NAME = "JM Wizzler";

    /**
     * Recently Played track constants.
     */
    String TRACK_ONE_ID = "2gNfxysfBRfl9Lvi9T3v6R";
    // Note: This is different than the Json, which is in GMT. See SpringSpotifyEventService for details.
    String TRACK_ONE_PLAYED_AT = "2016-12-13T15:44:04.000Z";
    Long TRACK_ONE_PLAYED_AT_TIMESTAMP = 1481661840L;
    String TRACK_ONE_ARTIST_ID = "5INjqkS1o8h1imAzPqGZBb";
    String TRACK_ONE_ARTIST_NAME = "Tame Impala";
    int TRACK_ONE_DURATION_MS = 108546;
    String TRACK_ONE_NAME = "Disciples";
    String TRACK_ONE_PREVIEW_URL = "https://p.scdn.co/mp3-preview/6023e5aac2123d098ce490488966b28838b14fa2";

    String TRACK_TWO_ID = "2X485T9Z5Ly0xyaghN73ed";
    // Note: This is different than the Json, which is in GMT. See SpringSpotifyEventService for details.
    String TRACK_TWO_PLAYED_AT = "2016-12-13T15:42:17.000Z";
    Long TRACK_TWO_PLAYED_AT_TIMESTAMP = 1481661720L;
    String TRACK_TWO_ARTIST_ID = "5INjqkS1o8h1imAzPqGZBb";
    String TRACK_TWO_ARTIST_NAME = "Tame Impala";
    int TRACK_TWO_DURATION_MS = 467586;
    String TRACK_TWO_NAME = "Let It Happen";
    String TRACK_TWO_PREVIEW_URL = "https://p.scdn.co/mp3-preview/05dee1ad0d2a6fa4ad07fbd24ae49d58468e8194";

    /**
     * Audio Features track constants.
     */
    float TRACK_ONE_DANCEABILITY = 0.808f;
    float TRACK_ONE_ENERGY = 0.626f;
    int TRACK_ONE_KEY = 7;
    float TRACK_ONE_LOUDNESS = -12.733f;
    int TRACK_ONE_MODE = 1;
    float TRACK_ONE_SPEECHINESS = 0.168f;
    float TRACK_ONE_ACOUSTICNESS = 0.00187f;
    float TRACK_ONE_INSTRUMENTALNESS = 0.159f;
    float TRACK_ONE_LIVENESS = 0.376f;
    float TRACK_ONE_VALENCE = 0.369f;
    float TRACK_ONE_TEMPO = 123.99f;
    int TRACK_ONE_TIME_SIGNATURE = 4;

    float TRACK_TWO_DANCEABILITY = 0.457f;
    float TRACK_TWO_ENERGY = 0.815f;
    int TRACK_TWO_KEY = 1;
    float TRACK_TWO_LOUDNESS = -7.199f;
    int TRACK_TWO_MODE = 1;
    float TRACK_TWO_SPEECHINESS = 0.034f;
    float TRACK_TWO_ACOUSTICNESS = 0.102f;
    float TRACK_TWO_INSTRUMENTALNESS = 0.0319f;
    float TRACK_TWO_LIVENESS = 0.103f;
    float TRACK_TWO_VALENCE = 0.382f;
    float TRACK_TWO_TEMPO = 96.083f;
    int TRACK_TWO_TIME_SIGNATURE = 4;

    // Since we're getting doubles from JSON, the ultimate values in java may not match exactly.
    float MARGIN_OF_ERROR = 0.00001f;

    /**
     * File constants. File Names are appended to the JSON_DIR.
     */
    String JSON_DIR = "src/test/resources/json";

    String AUTHORIZATION_CODE_JSON = "AuthorizationCode.json";

    String AUTHORIZATION_REFRESH_JSON = "AuthorizationCodeRefresh.json";

    String USER_PROFILE_JSON = "UserProfile.json";

    String RECENTLY_PLAYED_JSON = "RecentlyPlayedTracks.json";

    String AUDIO_FEATURES = "AudioFeatures.json";

}
