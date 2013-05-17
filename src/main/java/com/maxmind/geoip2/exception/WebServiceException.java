package com.maxmind.geoip2.exception;

public class WebServiceException extends HttpException {
    private static final long serialVersionUID = 8662062420258379643L;
    private String code;

    public WebServiceException(String message, String code, int http_status,
            String uri) {
        super(message, http_status, uri);
        this.code = code;
    }

    public WebServiceException(String message, String code, int http_status,
            String uri, Exception e) {
        super(message, http_status, uri, e);
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
