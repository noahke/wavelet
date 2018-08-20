package org.ciakraa.wavelet.web_api.spring;

import com.wrapper.spotify.IHttpManager;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.ciakraa.wavelet.web_api.SpotifyClientCredentials;
import org.ciakraa.wavelet.web_api.SpotifyUserCredentials;

import java.io.*;
import java.net.URI;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Simple abstract class to provide common functionality to unit tests.
 *
 * Test data and a few util methods courtesy of the Spotify Web API Java library.
 * @see <a href="https://github.com/thelinmichael/spotify-web-api-java/tree/master/examples">Spotify Web API Java: Examples</a>
 */
abstract class AbstractWebApiUnitTest implements WebApiTestConstants {

    SpotifyClientCredentials getClientCred() {
        return new DefaultSpotifyClientCredentials(CLIENT_ID, CLIENT_SECRET);
    }

    SpotifyUserCredentials getUserCred() {
        return new DefaultSpotifyUserCredentials.Builder()
                .setClientCred(getClientCred())
                .setUserId(USER_ID)
                .setUserDisplayName(USER_DISPLAY_NAME)
                .setAccessToken(ACCESS_TOKEN)
                .setRefreshToken(REFRESH_TOKEN)
                .build();
    }

    /**
     * Directly using the TestUtil.java class from Spotify WEB Api Java for the following methods,
     * since it'd be overkill to re-test that library's code. Thank you MIT License!
     *
     * @see <a href="https://github.com/thelinmichael/spotify-web-api-java/blob/master/src/test/java/com/wrapper/spotify/TestUtil.java">TestUtils.java</a>
     */
    static class MockedHttpManager {

        public static IHttpManager returningJson(String jsonFixture) throws Exception {

            // Mocked HTTP Manager to get predictable responses
            final IHttpManager mockedHttpManager = mock(IHttpManager.class);
            final String fixture = readTestData(jsonFixture);

            when(mockedHttpManager.get(any(URI.class), any(Header[].class))).thenReturn(fixture);
            when(mockedHttpManager.post(any(URI.class), any(Header[].class), any(HttpEntity.class))).thenReturn(fixture);
            when(mockedHttpManager.put(any(URI.class), any(Header[].class), any(HttpEntity.class))).thenReturn(fixture);
            when(mockedHttpManager.delete(any(URI.class), any(Header[].class), any(HttpEntity.class))).thenReturn(fixture);

            return mockedHttpManager;
        }
    }

    private static String readTestData(String fileName) throws IOException {
        return readFromFile(new File(JSON_DIR, fileName));
    }

    private static String readFromFile(File file) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
        StringBuilder out = new StringBuilder();
        String line;

        while ((line = in.readLine()) != null) {
            out.append(line);
        }

        in.close();

        return out.toString();
    }

    protected static String readFromFileTry(File file) {
        try {
            return readFromFile(file);
        } catch (IOException e) {
            return null;
        }
    }
}
