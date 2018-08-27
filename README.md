<b>Wavelet</b> is an event-streaming application for Spotify activity.

When a user registers for Wavelet via Spotify, <b>Wavelet-Publisher</b> will poll Spotify for the user's recently listened tracks every 10 minutes, along with the quantitative audio features for each track. The track and feature data are then published to an Apache Kafka topic.

Phase 2 of the project, <b>Wavelet-Stream</b>, will involve analyzing track events via Apache Flink. However, due to marathon training, this phase will develop very slowly! I will have a good chunk of dev-time available after my next marathon (NYC!), when I'm recovering (and hopefully celebrating!).

<br>

### Technical Overview: Wavelet-Publisher
<b>Wavelet-Publisher</b> is a Spring Boot app, with dependencies on Kafka for streaming and Redis for key-value storage. 

The app relies on <a href="https://developer.spotify.com/documentation/web-api/">Spotify Web API</a> for querying user activity. The <a href="https://github.com/thelinmichael/spotify-web-api-java">Spotify Web API Java</a> is a Java wrapper/client for accessing the Spotify Web API.

How does it work? Wavelet creates a poll task for each user and submits it to a <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/scheduling/TaskScheduler.html">Task Scheduler</a> to be executed every 10 minutes. Upon execution, the user's credentials are refreshed via Spotify Web API. Then, separate calls are made to the Spotify Web API to collect the user's Recently Listened Tracks, and each set of quantitative Audio Features for those tracks. After building an event object for each track/features, and checking Redis to filter out any previously seen listened tracks, the new track event is published to Kafka. Simple!

Here is an example of the json posted to Kafka:
```
{
  "userId": "noahke",
  "userDisplayName": "noahke",
  "key": "noahke-2X485T9Z5Ly0xyaghN73ed-1481661720",
  "trackId": "2X485T9Z5Ly0xyaghN73ed",
  "playedAt": 1481661720,
  "artistName": "Kraftwerk",
  "artistId": "5INjqkS1o8h1imAzPqGZBb",
  "durationMs": 467586,
  "name": "Autobahn",
  "previewUrl": "https://p.scdn.co/mp3-preview/05dee1ad0d2a6fa4ad07fbd24ae49d58468e8194",
  "acousticness": 0.10199999809265137,
  "danceability": 0.4569999873638153,
  "energy": 0.8149999976158142,
  "instrumentalness": 0.03189999982714653,
  "keySignature": 1,
  "liveness": 0.10300000011920929,
  "loudness": -7.198999881744385,
  "mode": 1,
  "speechiness": 0.03400000184774399,
  "tempo": 96.08300018310547,
  "timeSignature": 4,
  "valence": 0.38199999928474426
}
```

The hearty vegetables of this code can be found in these classes:
- ListenedTrackPoll: executes the aforementioned code flow every 10 minutes.
- EventService: fetches the recently listened tracks + audio features, via SpotifyActivityService, and prepares them for Kafka publishing.
- SpotifyActivityService: executes calls to Spotify Web API.

User registration and credentials, necessary for the Spotify Web API, is dependent on the <a href="https://developer.spotify.com/documentation/general/guides/authorization-guide/">Spotify Web API Code Authorization Flow</a>.

From a high-level: the user visits Wavelet's home page in their browser. They are directed to Spotify, who confirms that the user wants to grant permissions to Wavelet. Upon confirmation, the user is redirected to Wavelet with a unique code identifier. Using this code identifier, Wavelet calls Spotify Web API and is given user client credentials, which can be used for API calls related to that user's data. The user client credentials have to be periodically refreshed via a refresh token provided with the client credentials.

The hearty veggies of this flow are found in:
- SpotifyAuthorizationService: responsible for all user credential interactions with Spotify Web API.

(I suggest reading Spotify's documentation for this flow, as it can be kind of confusing!)


### How to run the app: Wavelet-Publisher

The app was developed with Kafka 2.11 and Redis 4.0.9.

You will need a application.properties file in src/main/resources/ that looks like this:

```
spotify.clientId= Your Client ID
spotify.clientSecret= Your Client Secret
spotify.redirectUri=http://localhost:8080/wavelet/authorize
spotify.userScopes=user-read-recently-played,user-read-email

redis.host=localhost
redis.port=6379

kafka.server=localhost:9092
spring.kafka.consumer.group-id=wavelet
```

The same properties file is needed in src/test/resources, as test.properties.

To run the app, Kafka and Redis need to be running. Here's a cheat-sheet of commands to get those going:

```
 // Start Zookeeper
> zookeeper-server-start.sh /path/to/kafka_2.11-1.1.0/config/zookeeper.properties

// Start Kafka
> kafka-server-start.sh /path/to/kafka_2.11-1.1.0/config/server.properties

// Create a topic consumer for Kafka, if you want to see events as they're published:
> kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic listenedTracks

// Start Redis
> redis-server
```
 
To build the app, with tests running:
```
mvn clean install
```

To run the app:
```
mvn spring-boot:run -Dspring.profiles.active=prod
```

