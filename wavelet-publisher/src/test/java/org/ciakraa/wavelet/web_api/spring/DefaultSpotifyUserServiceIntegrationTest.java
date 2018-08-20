package org.ciakraa.wavelet.web_api.spring;

import org.ciakraa.wavelet.web_api.SpotifyUserCredentials;
import org.ciakraa.wavelet.web_api.SpotifyUserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisOperations;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Make sure redis is running when this test runs!
 */
@Profile("integration")
public final class DefaultSpotifyUserServiceIntegrationTest extends AbstractWebApiIntegrationTest {

    @Autowired
    private RedisOperations<String, Object> redis;

    @Autowired
    private SpotifyUserService userService;

    @Before
    @After
    public void clearKeys() {
        redis.boundHashOps(USERS_KEY).delete(USER_ID);
    }

    @Test
    public void shouldSaveCredToRedis() {
        SpotifyUserCredentials userCred = getUserCred();
        userService.save(userCred);

        assertThat(redis.boundHashOps(USERS_KEY).get(USER_ID)).isNotNull();
    }

    @Test
    public void shouldFindAllCredInRedis() {
        SpotifyUserCredentials userCred = getUserCred();
        userService.save(userCred);

        SpotifyUserCredentials savedCred = userService.findAll().stream().findFirst().get();
        assertThat(savedCred).isEqualTo(userCred);
    }


}
