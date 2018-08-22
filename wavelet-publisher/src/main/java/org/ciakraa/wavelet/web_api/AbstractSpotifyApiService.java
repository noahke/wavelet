package org.ciakraa.wavelet.web_api;

import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.exceptions.detailed.TooManyRequestsException;
import com.wrapper.spotify.exceptions.detailed.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Abstract class to support any services that rely on querying Spotify API.
 * Mainly concerned with exception handling.
 */
abstract class AbstractSpotifyApiService {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractSpotifyApiService.class);

    /**
     * Any call to execute SpotifyAPI and it's request objects will satisfy this functional interface.
     */
    @FunctionalInterface
    interface SpotifyApiSupplier<T> {
        T get() throws IOException, SpotifyWebApiException;
    }

    /**
     * For ease of simplicity in this here hobby project, our services won't be too granular with responding to Spotify API status codes.
     * Here's how we handle the status codes:
     *
     * 429: "Rate Limiting Has Been Applied". Sleep the thread until we can try again, with a finite number of tries.
     * 401: "Unauthorized Access". The service caller should handle this, likely with refreshing an access token.
     * Everything else: Return an empty result.
     */
    <T> Optional<T> executeWithAccess(SpotifyApiSupplier<T> supplier) throws SpotifyUnauthorizedException {
        return executeWithAccess(supplier, 2);
    }

    <T> Optional<T> executeWithAccess(SpotifyApiSupplier<T> supplier, int retries) throws SpotifyUnauthorizedException {
        if (retries < 0) {
            LOG.error("Unable to execute spotify request due to no more retries.");
            return Optional.empty();
        }

        try {
            return Optional.ofNullable(supplier.get());
        } catch (TooManyRequestsException exp) {
            // Spotify rounds down retry seconds, e.g. 32000ms to 3 seconds, so add one to be safe.
            LOG.info("Spotify has returned a Too Many Requests status. Waiting {} seconds. {} retries remain.", exp.getRetryAfter() + 1, retries);
            return retryWithAccess(supplier, exp.getRetryAfter() + 1, --retries);
        } catch(UnauthorizedException exp) {
            LOG.error("Spotify request execution with access failed due to unauthorized error:", exp);
            throw new SpotifyUnauthorizedException();
        } catch (IOException | SpotifyWebApiException exp) {
            LOG.error("Spotify request execution with access failed due to exception:", exp);
            return Optional.empty();
        }
    }

    <T> Optional<T> retryWithAccess(SpotifyApiSupplier<T> supplier, int retryAfter, int retries) throws SpotifyUnauthorizedException {
        try {
            TimeUnit.SECONDS.sleep(retryAfter);
        } catch (InterruptedException exp) {
            LOG.error("Spotify retry interrupted", exp);
            Thread.currentThread().interrupt();
        }
        return executeWithAccess(supplier, --retries);
    }

    /**
     * Not every API call needs access tokens + client id/secrets. These are their methods. *Law and Order: SVU noise*
     */
    <T> Optional<T> executeWithoutAccess(SpotifyApiSupplier<T> supplier)  {
        return executeWithoutAccess(supplier, 2);
    }

    <T> Optional<T> executeWithoutAccess(SpotifyApiSupplier<T> supplier, int retries) {
        if (retries < 0) {
            LOG.error("Unable to execute spotify request due to no more retries.");
            return Optional.empty();
        }

        try {
            return Optional.ofNullable(supplier.get());
        } catch (TooManyRequestsException exp) {
            // Spotify rounds down retry seconds, e.g. 32000ms to 3 seconds, so add one to be safe.
            LOG.info("Spotify has returned a Too Many Requests status. Waiting {} seconds. {} retries remain.", exp.getRetryAfter() + 1, retries);
            return retryWithoutAccess(supplier, exp.getRetryAfter() + 1, --retries);
        }  catch (IOException | SpotifyWebApiException exp) {
            LOG.error("Spotify request execution without access failed due to exception:", exp);
            return Optional.empty();
        }
    }

    <T> Optional<T> retryWithoutAccess(SpotifyApiSupplier<T> supplier, int retryAfter, int retries) {
        try {
            TimeUnit.SECONDS.sleep(retryAfter);
        } catch (InterruptedException exp) {
            LOG.error("Spotify retry interrupted", exp);
            Thread.currentThread().interrupt();
        }
        return executeWithoutAccess(supplier, --retries);
    }

}
