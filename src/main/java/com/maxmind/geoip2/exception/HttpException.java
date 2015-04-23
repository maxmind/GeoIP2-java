package com.maxmind.geoip2.exception;

import java.io.IOException;
import java.net.URL;

/**
 * This class represents an HTTP transport error. This is not an error returned
 * by the web service itself. As such, it is a IOException instead of a
 * GeoIp2Exception.
 */
public final class HttpException extends IOException {
    private static final long serialVersionUID = -8301101841509056974L;
    private final int httpStatus;
    private final URL url;

    /**
     * @param message    A message describing the reason why the exception was thrown.
     * @param httpStatus The HTTP status of the response that caused the exception.
     * @param url        The URL queried.
     */
    public HttpException(String message, int httpStatus, URL url) {
        super(message);
        this.httpStatus = httpStatus;
        this.url = url;
    }

    /**
     * @param message    A message describing the reason why the exception was thrown.
     * @param httpStatus The HTTP status of the response that caused the exception.
     * @param url        The URL queried.
     * @param cause      The cause of the exception.
     */
    public HttpException(String message, int httpStatus, URL url,
                         Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
        this.url = url;
    }

    /**
     * @return the HTTP status of the query that caused the exception.
     */
    public int getHttpStatus() {
        return this.httpStatus;
    }

    /**
     * @return the URL queried.
     */
    public URL getUrl() {
        return this.url;
    }
}
