package com.maxmind.geoip2.exception;

import java.io.IOException;
import java.net.URI;

/**
 * This class represents an HTTP transport error. This is not an error returned
 * by the web service itself. As such, it is a IOException instead of a
 * GeoIp2Exception.
 */
public final class HttpException extends IOException {
    private static final long serialVersionUID = -8301101841509056974L;
    private final int httpStatus;
    private final URI uri;

    /**
     * @param message    A message describing the reason why the exception was thrown.
     * @param httpStatus The HTTP status of the response that caused the exception.
     * @param uri        The URI queried.
     */
    public HttpException(String message, int httpStatus, URI uri) {
        super(message);
        this.httpStatus = httpStatus;
        this.uri = uri;
    }

    /**
     * @param message    A message describing the reason why the exception was thrown.
     * @param httpStatus The HTTP status of the response that caused the exception.
     * @param uri        The URI queried.
     * @param cause      The cause of the exception.
     */
    public HttpException(String message, int httpStatus, URI uri,
                         Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
        this.uri = uri;
    }

    /**
     * @return the HTTP status of the query that caused the exception.
     */
    public int getHttpStatus() {
        return this.httpStatus;
    }

    /**
     * @return the URI queried.
     */
    public URI getUri() {
        return this.uri;
    }


}
