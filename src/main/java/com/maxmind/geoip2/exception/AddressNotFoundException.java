package com.maxmind.geoip2.exception;

/**
 * This exception is thrown when the IP address is not found in the database.
 * This generally means that the address was a private or reserved address.
 */
public final class AddressNotFoundException extends GeoIp2Exception {

    private static final long serialVersionUID = -639962574626980783L;

    /**
     * @param message A message explaining the cause of the error.
     */
    public AddressNotFoundException(String message) {
        super(message);
    }

    /**
     * @param message A message explaining the cause of the error.
     * @param e       The cause of the exception.
     */
    public AddressNotFoundException(String message, Throwable e) {
        super(message, e);
    }
}
