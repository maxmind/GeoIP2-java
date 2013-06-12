package com.maxmind.geoip2.exception;

/**
 * This class represents an error returned by MaxMind's GeoIP2 web service.
 */
public class AddressNotFoundException extends GeoIP2Exception {
    private static final long serialVersionUID = 5524955504419835710L;

    /**
     * @param message
     *            A message explaining the cause of the error.
     */
    public AddressNotFoundException(String message) {
        super(message);
    }

    /**
     * @param message
     *            A message explaining the cause of the error.
     * @param e
     *            The cause of the exception.
     */
    public AddressNotFoundException(String message, Throwable e) {
        super(message, e);
    }
}
