package org.ciakraa.wavelet.web_api;

import org.apache.commons.lang3.Validate;
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
 * Service responsible for managing users once they have been authorized via {@link SpotifyAuthorizationService}
 *
 * User credentials are stored in Redis as a hash.
 * Each user hash is stored in the "users" hash by the user id.
 *
 * (Basically, it's a Map<String, SpotifyUserCredentials> so we can only have one user cred json per user)
 */
@Service
public class SpotifyUserService {

    private static final Logger LOG = LoggerFactory.getLogger(SpotifyAuthorizationService.class);

    private final BoundHashOperations<String, String, Map<String, Object>> users;
    private final Jackson2HashMapper mapper;

    @Autowired
    SpotifyUserService(RedisOperations<String, Object> redis) {
        this.users = redis.boundHashOps(WebApiConstants.USERS_KEY);
        this.mapper = new Jackson2HashMapper(false);
    }

    /**
     * Retrieves all saved user credentials for this app.
     */
    public Set<SpotifyUserCredentials> findAll() {
        Set<SpotifyUserCredentials> all = users.entries().values()
                .stream()
                .map(this::mapHash)
                .collect(toSet());

        LOG.info("Retrieved saved user credentials: {}", all);
        return all;
    }

    /**
     * Saves user credentials on a per user basis. Will overwrite existing cred for a user.
     */
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
