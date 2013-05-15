package com.maxmind.geoip2.exception;

public class GeoIP2Exception extends Exception {

    private static final long serialVersionUID = -1923104535309628719L;

    public GeoIP2Exception(String message) {
        super(message);
    }

    public GeoIP2Exception(String message, Throwable cause) {
        super(message, cause);
    }
}
