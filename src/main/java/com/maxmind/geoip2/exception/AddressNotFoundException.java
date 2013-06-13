package com.maxmind.geoip2.exception;

/**
 * This exception is thrown when the IP address is not found in the database.
 * This generally means that the address was a private or reserved address.
 */
public class AddressNotFoundException extends Exception {
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
