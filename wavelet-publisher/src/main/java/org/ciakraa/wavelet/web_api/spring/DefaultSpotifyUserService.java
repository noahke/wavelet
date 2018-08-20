package org.ciakraa.wavelet.web_api.spring;

import org.apache.commons.lang3.Validate;
import org.ciakraa.wavelet.web_api.SpotifyUserCredentials;
import org.ciakraa.wavelet.web_api.SpotifyUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.hash.Jackson2HashMapper;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

/**
 * User credentials are stored in Redis as a hash.
 * Each user hash is stored in the "users" hash by the user id.
 *
 * (Basically, it's a Map<String, SpotifyUserCredentials> so we can only have one user cred json per user)
 */
@Service
public class DefaultSpotifyUserService implements SpotifyUserService, WebApiConstants {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultAuthorizationService.class);

    private final BoundHashOperations<String, String, Map<String, Object>> users;
    private final Jackson2HashMapper mapper;

    @Autowired
    DefaultSpotifyUserService(RedisOperations<String, Object> redis) {
        this.users = redis.boundHashOps(USERS_KEY);
        this.mapper = new Jackson2HashMapper(false);
    }

    @Override
    public Set<SpotifyUserCredentials> findAll() {
        Set<SpotifyUserCredentials> all = users.entries().values()
                .stream()
                .map(this::mapHash)
                .collect(toSet());

        LOG.info("Retrieved saved user credentials: {}", all);
        return all;
    }

    @Override
    public void save(SpotifyUserCredentials userCred) {
        Validate.isTrue(userCred.validate());

        Map<String, Object> hash = writeHash(userCred);
        users.put(userCred.getUserId(), hash);
    }

    private SpotifyUserCredentials mapHash(Map<String, Object> loadedHash) {
        return (SpotifyUserCredentials) mapper.fromHash(loadedHash);
    }

    private Map<String, Object>  writeHash(SpotifyUserCredentials userCred) {
        return mapper.toHash(userCred);
    }

}
