package com.maxmind.geoip2.exception;

import java.net.URI;

/**
 * This class represents a non-specific error returned by MaxMind's GeoIP2 web
 * service. This occurs when the web service is up and responding to requests,
 * but the request sent was invalid in some way.
 */
public final class InvalidRequestException extends GeoIp2Exception {
    private final String code;
    private final URI uri;

    /**
     * @param message A message explaining the cause of the error.
     * @param code    The error code returned by the web service.
     * @param uri     The URI queried.
     */
    public InvalidRequestException(String message, String code, URI uri) {
        super(message);
        this.uri = uri;
        this.code = code;
    }

    /**
     * @param message    A message explaining the cause of the error.
     * @param code       The error code returned by the web service.
     * @param httpStatus The HTTP status of the response.
     * @param uri        The URI queried.
     * @param e          The cause of the exception.
     */
    public InvalidRequestException(String message, String code, int httpStatus,
                                   URI uri, Throwable e) {
        super(message, e);
        this.code = code;
        this.uri = uri;
    }

    /**
     * @return The error code returned by the MaxMind web service.
     */
    public String code() {
        return this.code;
    }

    /**
     * @return The error code returned by the MaxMind web service.
     * @deprecated Use {@link #code()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    public String getCode() {
        return code();
    }

    /**
     * @return the URI queried.
     */
    public URI uri() {
        return this.uri;
    }

    /**
     * @return the URI queried.
     * @deprecated Use {@link #uri()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    public URI getUri() {
        return uri();
    }

}
