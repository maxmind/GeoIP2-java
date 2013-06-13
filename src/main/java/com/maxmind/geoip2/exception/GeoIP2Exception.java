package com.maxmind.geoip2.exception;

import java.io.IOException;

/**
 * This class represents a generic GeoIP2 error. All other exceptions thrown by
 * the GeoIP2 API subclass this exception
 */
public class GeoIP2Exception extends IOException {

    private static final long serialVersionUID = -1923104535309628719L;

    /**
     * @param message
     *            A message describing the reason why the exception was thrown.
     */
    public GeoIP2Exception(String message) {
        super(message);
    }

    /**
     * @param message
     *            A message describing the reason why the exception was thrown.
     * @param cause
     *            The cause of the exception.
     */
    public GeoIP2Exception(String message, Throwable cause) {
        super(message, cause);
    }
}
