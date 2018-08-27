package org.ciakraa.wavelet.event;

import com.wrapper.spotify.model_objects.specification.AudioFeatures;
import com.wrapper.spotify.model_objects.specification.PlayHistory;
import com.wrapper.spotify.model_objects.specification.TrackSimplified;
import org.ciakraa.wavelet.web_api.SpotifyActivityService;
import org.ciakraa.wavelet.web_api.SpotifyUnauthorizedException;
import org.ciakraa.wavelet.web_api.SpotifyUserCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

/**
 * This service will fetch {@link UserEvent} from Spotify via {@link SpotifyActivityService}.
 * Redis caches are used to guarantee unique events, as Spotify queries can return the same event multiple times.
 */
@Service
public class ListenedTrackService {

    private final SpotifyActivityService activityService;
    private final RedisOperations<String, Object> redis;

    @Autowired
    public ListenedTrackService(SpotifyActivityService activityService, RedisOperations<String, Object> redis) {
        this.activityService = activityService;
        this.redis = redis;
    }

    /**
     * Retrieves the n latest {@link ListenedTrack} for a given Spotify user.
     *
     * Only new events are fetched; if we have already seen an event (thanks to previous polling), we will not
     * return it again.
     *
     * @param userCredentials
     * @param count
     * @return listenedTracks
     */
    public List<ListenedTrack> getUniqueRecentlyListened(SpotifyUserCredentials userCred, int count) {
        List<PlayHistory> plays = getPlays(userCred, count);
        if (plays.isEmpty()) {
            return emptyList();
        }

        Map<String, AudioFeatures> featuresByTrack = getAudioFeatures(userCred, plays);
        if (featuresByTrack.isEmpty()) {
            return emptyList();
        }

        Set<String> cachedTracksKeys = getCachedTracksKeys(userCred);
        List<ListenedTrack> newTracks = getListenedTracks(userCred, plays, featuresByTrack).stream()
                .filter(track -> !cachedTracksKeys.contains(track.getKey()))
                .collect(toList());

        updateCache(newTracks, userCred);

        return newTracks;
    }

    private List<PlayHistory> getPlays(SpotifyUserCredentials userCred, int count) {
        try {
            return activityService.getRecentlyListened(userCred, count);
        } catch (SpotifyUnauthorizedException e) {
            return emptyList();
        }
    }

    private Map<String, AudioFeatures> getAudioFeatures(SpotifyUserCredentials userCred, List<PlayHistory> listenedTracks) {
        List<String> listenedTrackIds = listenedTracks.stream()
                .map(PlayHistory::getTrack)
                .map(TrackSimplified::getId)
                .distinct()
                .collect(toList());

        List<AudioFeatures> features = new ArrayList<>();
        try {
            features = activityService.getAudioFeatures(userCred, listenedTrackIds);
        } catch (SpotifyUnauthorizedException e) {
            return emptyMap();
        }

        return features.stream().collect(toMap(AudioFeatures::getId, Function.identity()));
    }

    private Set<String> getCachedTracksKeys(SpotifyUserCredentials userCred) {
        return redis.boundZSetOps(getTrackCacheKey(userCred)).range(0, -1).stream()
                .map(track -> (String) track)
                .collect(toSet());
    }

    private List<ListenedTrack> getListenedTracks(SpotifyUserCredentials userCred, List<PlayHistory> plays,
                                                         Map<String, AudioFeatures> featuresByTrack) {
        Function<PlayHistory, ListenedTrack> mapper = play -> {
            ListenedTrack track = new ListenedTrack();
            track.setKey(getListenedTrackKey(play, userCred));
            track.setUserId(userCred.getUserId());
            track.setUserDisplayName(userCred.getUserDisplayName());
            track.setTrackId(play.getTrack().getId());
            track.setPlayedAt(getTimestamp(play.getPlayedAt()));
            track.setArtistId(play.getTrack().getArtists()[0].getId());
            track.setArtistName(play.getTrack().getArtists()[0].getName());
            track.setDurationMs(play.getTrack().getDurationMs());
            track.setName(play.getTrack().getName());
            track.setPreviewUrl(play.getTrack().getPreviewUrl());
            
            AudioFeatures features = featuresByTrack.get(play.getTrack().getId());
            track.setAcousticness(features.getAcousticness());
            track.setDanceability(features.getDanceability());
            track.setEnergy(features.getEnergy());
            track.setInstrumentalness(features.getInstrumentalness());
            track.setKeySignature(features.getKey());
            track.setLiveness(features.getLiveness());
            track.setLoudness(features.getLoudness());
            track.setMode(features.getMode().getType());
            track.setSpeechiness(features.getSpeechiness());
            track.setTempo(features.getTempo());
            track.setTimeSignature(features.getTimeSignature());
            track.setValence(features.getValence());

            return track;
        };

        return plays.stream().map(mapper).collect(toList());
    }

    /**
     * Spotify counts tracks as "listened to" if the user listens for >= 30 seconds.
     * This means each call to Spotify will return a maximum of recently listened tracks:
     * Poll Period (in seconds) / 30s = MAX_RECENTLY_LISTENED_TO
     *
     * Thus, we only need to keep the last MAX_RECENTLY_LISTENED_TO count of tracks in memory.
     */
    private void updateCache(List<ListenedTrack> tracks, SpotifyUserCredentials userCred) {
        BoundZSetOperations userCache = redis.boundZSetOps(getTrackCacheKey(userCred));
        if (userCache.zCard() == EventConstants.MAX_RECENTLY_LISTENED_TO) {
            userCache.removeRange(0L, tracks.size() - 1L);
        }

        for (ListenedTrack track : tracks) {
            userCache.add(track.getKey(), track.getPlayedAt());
        }
    }

    static String getListenedTrackKey(PlayHistory play, SpotifyUserCredentials userCred) {
        return userCred.getUserId() + "-" + play.getTrack().getId() + "-" + getTimestamp(play.getPlayedAt());
    }

    static String getTrackCacheKey(SpotifyUserCredentials userCred) {
        return EventConstants.RECENTLY_LISTENED_KEY_PREFIX + userCred.getUserId();
    }
    
    static long getTimestamp(Date date) {
        return date.toInstant().atZone(ZoneId.of("GMT")).truncatedTo(ChronoUnit.MINUTES).toEpochSecond();
    }

}
