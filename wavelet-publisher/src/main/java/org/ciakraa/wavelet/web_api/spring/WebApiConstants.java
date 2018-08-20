package org.ciakraa.wavelet.web_api.spring;

import org.ciakraa.wavelet.web_api.SpotifyConstants;

interface WebApiConstants extends SpotifyConstants {

    /**
     * Redis key for a set of state strings.
     */
    String STATES_KEY = "states";

    /**
     * States are Strings with 20 randomly generated alphanumeric characters.
     */
    int STATE_LENGTH = 20;

    /**
     * Redis key prefix for user credentials.
     */
    String USERS_KEY = "users";

}
