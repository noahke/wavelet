package org.ciakraa.wavelet.web_api;

import java.util.Set;

/**
 * Service responsible for managing users once they have been authorized via {@link SpotifyAuthorizationService}
 */
public interface SpotifyUserService {

    /**
     * Retrieves all saved user credentials for this app.
     */
    Set<SpotifyUserCredentials> findAll();

    /**
     * Saves user credentials on a per user basis. Will overwrite existing cred for a user.
     */
    void save(SpotifyUserCredentials userCred);
}
