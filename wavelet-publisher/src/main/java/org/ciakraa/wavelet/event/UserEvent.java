package org.ciakraa.wavelet.event;

/**
 * Model for any Spotify event involving a user.
 *
 * Extending classes must meet the following criteria to be eligible for Kafka + Flink processing:
 * 1. All fields must be exposed by getters/setters.
 * 2. Must have a public constructor without arguments.
 * 3. Class must be public.
 * 4. Fields must have a type supported by Apache Avro.
 *
 * Methods are final to encourage POJO-ness in extending classes.
 * There should be no logic in getters or setters.
 *
 * @see <a href="https://ci.apache.org/projects/flink/flink-docs-release-1.4/dev/api_concepts.html#supported-data-types">Flink Supported Datatypes</a>
 * @see <a href="https://avro.apache.org/docs/current/spec.html">Avro Specification</a>
 */
public abstract class UserEvent {

    private String userId;
    private String userDisplayName;

    public UserEvent() {}

    public final String getUserId() {
        return userId;
    }

    public final void setUserId(String userId) {
        this.userId = userId;
    }

    public final String getUserDisplayName() {
        return userDisplayName;
    }

    public final void setUserDisplayName(String displayName) {
        this.userDisplayName = displayName;
    }

}
