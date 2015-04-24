package com.maxmind.geoip2.exception;

/**
 * This exception is thrown when your account does not have any queries
 * remaining for the called service.
 */
public final class OutOfQueriesException extends GeoIp2Exception {
    private static final long serialVersionUID = 3843736987256336967L;

    /**
     * @param message A message explaining the cause of the error.
     */
    public OutOfQueriesException(String message) {
        super(message);
    }

    /**
     * @param message A message explaining the cause of the error.
     * @param e       The cause of the exception.
     */
    public OutOfQueriesException(String message, Throwable e) {
        super(message, e);
    }
}
