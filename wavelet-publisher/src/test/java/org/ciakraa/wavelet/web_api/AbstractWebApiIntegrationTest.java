package org.ciakraa.wavelet.web_api;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.ciakraa.wavelet.common.CommonTestConstants.*;

@SpringBootTest
@Profile("integration")
@RunWith(SpringRunner.class)
@TestPropertySource(locations="classpath:test.properties")
abstract class AbstractWebApiIntegrationTest {

    @Autowired
    SpotifyClientCredentials clientCred;

    SpotifyUserCredentials getUserCred() {
        return new SpotifyUserCredentials.Builder()
                .setUserId(USER_ID)
                .setUserDisplayName(USER_DISPLAY_NAME)
                .setAccessToken(ACCESS_TOKEN)
                .setRefreshToken(REFRESH_TOKEN)
                .setClientCred(clientCred)
                .build();
    }
}
