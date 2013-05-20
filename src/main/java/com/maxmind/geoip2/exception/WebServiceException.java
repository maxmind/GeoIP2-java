package com.maxmind.geoip2.exception;

import java.net.URL;

/**
 * This class represents an error returned by MaxMind's GeoIP2 web service.
 */
public class WebServiceException extends HttpException {
    private static final long serialVersionUID = 8662062420258379643L;
    private final String code;

    /**
     * @param message
     *            A message explaining the cause of the error.
     * @param code
     *            The error code returned by the web service.
     * @param httpStatus
     *            The HTTP status of the response.
     * @param url
     *            The URL queried.
     */
    public WebServiceException(String message, String code, int httpStatus,
            URL url) {
        super(message, httpStatus, url);
        this.code = code;
    }

    /**
     * @param message
     *            A message explaining the cause of the error.
     * @param code
     *            The error code returned by the web service.
     * @param httpStatus
     *            The HTTP status of the response.
     * @param url
     *            The URL queried.
     * @param e
     *            The cause of the exception.
     */
    public WebServiceException(String message, String code, int httpStatus,
            URL url, Throwable e) {
        super(message, httpStatus, url, e);
        this.code = code;
    }

    /**
     * @return The error code returned by the MaxMind web service.
     */
    public String getCode() {
        return this.code;
    }
}
