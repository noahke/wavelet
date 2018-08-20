package org.ciakraa.wavelet.web_api.spring;

import org.ciakraa.wavelet.web_api.SpotifyUserCredentials;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.hash.Jackson2HashMapper;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.*;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultSpotifyUserServiceUnitTest extends AbstractWebApiUnitTest {

    @Mock
    private RedisOperations<String, Object> redis;

    @Mock
    private BoundHashOperations<String, Object, Object> users;

    private DefaultSpotifyUserService target;

    @BeforeMethod
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockRedis();

        target = new DefaultSpotifyUserService(redis);
    }

    @Test
    void shouldReturnEmptySetWhenFindAllReturnsNoUserCredentials() {
        when(users.entries()).thenReturn(emptyMap());

        assertThat(target.findAll()).isEmpty();
    }

    @Test
    void shouldReturnAllSavedUserCred() {
        SpotifyUserCredentials userCred = getUserCred();
        Jackson2HashMapper mapper = new Jackson2HashMapper(false);
        Map<String, Object> userCredHash = mapper.toHash(userCred);


        Map<Object, Object> usersHash = new HashMap<>();
        usersHash.put(USER_ID, userCredHash);
        when(users.entries()).thenReturn(usersHash);

        Set<SpotifyUserCredentials> result = target.findAll();
        assertThat(result).containsOnly(userCred);
    }

    @Test
    void shouldThrowExceptionWhenSavingInvalidCred() {
        assertThatNullPointerException().isThrownBy(() -> target.save(null));
        assertThatIllegalArgumentException().isThrownBy(() -> target.save(mock(SpotifyUserCredentials.class)));
    }

    private void mockRedis() {
        when(redis.boundHashOps(USERS_KEY)).thenReturn(users);
    }
}
