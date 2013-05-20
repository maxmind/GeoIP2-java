package com.maxmind.geoip2.exception;

public class HttpException extends GeoIP2Exception {
    private static final long serialVersionUID = -8301101841509056974L;
    private final int http_status;
    private final String uri;

    public HttpException(String message, int http_status, String uri) {
        super(message);
        this.http_status = http_status;
        this.uri = uri;
    }

    public HttpException(String message, int http_status, String uri,
            Exception e) {
        super(message, e);
        this.http_status = http_status;
        this.uri = uri;
    }

    public HttpException(String message, Throwable cause, int http_status,
            String uri) {
        super(message, cause);
        this.http_status = http_status;
        this.uri = uri;
    }

    public int getHttpStatus() {
        return this.http_status;
    }

    public String getUri() {
        return this.uri;
    }
}
