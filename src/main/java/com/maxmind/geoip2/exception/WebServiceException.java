package com.maxmind.geoip2.exception;

import java.net.URL;

public class WebServiceException extends HttpException {
    private static final long serialVersionUID = 8662062420258379643L;
    private final String code;

    public WebServiceException(String message, String code, int http_status,
            URL url) {
        super(message, http_status, url);
        this.code = code;
    }

    public WebServiceException(String message, String code, int http_status,
            URL url, Throwable e) {
        super(message, http_status, url, e);
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
