package com.maxmind.geoip2.exception;

/**
 * This exception is thrown when there is an authentication error.
 */
public final class AuthenticationException extends GeoIp2Exception {

    private static final long serialVersionUID = 2255398691576141427L;

    /**
     * @param message A message explaining the cause of the error.
     */
    public AuthenticationException(String message) {
        super(message);
    }

    /**
     * @param message A message explaining the cause of the error.
     * @param e       The cause of the exception.
     */
    public AuthenticationException(String message, Throwable e) {
        super(message, e);
    }
}
