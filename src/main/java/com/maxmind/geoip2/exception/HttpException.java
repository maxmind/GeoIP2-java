package com.maxmind.geoip2.exception;

import java.net.URL;

public class HttpException extends GeoIP2Exception {
    private static final long serialVersionUID = -8301101841509056974L;
    private final int http_status;
    private final URL url;

    public HttpException(String message, int http_status, URL uri) {
        super(message);
        this.http_status = http_status;
        this.url = uri;
    }

    public HttpException(String message, int http_status, URL url,
            Throwable cause) {
        super(message, cause);
        this.http_status = http_status;
        this.url = url;
    }

    public int getHttpStatus() {
        return this.http_status;
    }

    public URL getUrl() {
        return this.url;
    }
}
