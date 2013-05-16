package com.maxmind.geoip2.exception;

public class HttpException extends GeoIP2Exception {
    private static final long serialVersionUID = -8301101841509056974L;
    private int http_status;
    private String uri;

    public HttpException(String message, int http_status, String uri) {
        super(message);
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
        return http_status;
    }

    public String getUri() {
        return uri;
    }
}
