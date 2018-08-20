package org.ciakraa.wavelet.event.spring;

import org.ciakraa.wavelet.common.CommonTestConstants;
import org.ciakraa.wavelet.common.CommonUnitTest;
import org.ciakraa.wavelet.web_api.SpotifyClientCredentials;
import org.ciakraa.wavelet.web_api.SpotifyUserCredentials;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@Profile("integration")
@RunWith(SpringRunner.class)
@TestPropertySource(locations="classpath:test.properties")
public abstract class AbstractEventIntegrationTest extends CommonUnitTest implements CommonTestConstants, EventConstants {

    @Autowired
    SpotifyClientCredentials clientCred;

    SpotifyUserCredentials getUserCred() {
        SpotifyUserCredentials cred = mock(SpotifyUserCredentials.class);
        when(cred.getAccessToken()).thenReturn(ACCESS_TOKEN);
        when(cred.getUserId()).thenReturn(USER_ID);
        when(cred.getUserDisplayName()).thenReturn(USER_DISPLAY_NAME);
        when(cred.getRefreshToken()).thenReturn(REFRESH_TOKEN);
        when(cred.getClientCred()).thenReturn(clientCred);
        return cred;
    }
}
