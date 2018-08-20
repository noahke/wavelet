package org.ciakraa.wavelet.web_api.spring;

import org.ciakraa.wavelet.web_api.SpotifyClientCredentials;
import org.ciakraa.wavelet.web_api.SpotifyUserCredentials;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@Profile("integration")
@RunWith(SpringRunner.class)
@TestPropertySource(locations="classpath:test.properties")
class AbstractWebApiIntegrationTest implements WebApiTestConstants {

    @Autowired
    SpotifyClientCredentials clientCred;

    SpotifyUserCredentials getUserCred() {
        return new DefaultSpotifyUserCredentials.Builder()
                .setUserId(USER_ID)
                .setUserDisplayName(USER_DISPLAY_NAME)
                .setAccessToken(ACCESS_TOKEN)
                .setRefreshToken(REFRESH_TOKEN)
                .setClientCred(clientCred)
                .build();
    }
}
