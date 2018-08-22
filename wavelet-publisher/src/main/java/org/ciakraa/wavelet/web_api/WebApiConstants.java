package org.ciakraa.wavelet.web_api;

final class WebApiConstants {

    private WebApiConstants() {
        // Static values class doesn't need to be instantiated!
    }

    /**
     * The max number of recently listened tracks that Spotify will return.
     */
    static final int MAX_RECENTLY_LISTENED_TO = 50;

    /**
     * Redis key for a set of state strings.
     */
    static final String STATES_KEY = "states";

    /**
     * States are Strings with 20 randomly generated alphanumeric characters.
     */
    static final int STATE_LENGTH = 20;

    /**
     * Redis key prefix for user credentials.
     */
    static final String USERS_KEY = "users";

}
