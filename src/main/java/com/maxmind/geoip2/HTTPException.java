package com.maxmind.geoip2;

public class HTTPException extends GenericException {
  private int http_status;
  private String uri;

  HTTPException(String message,int http_status,String uri) {
    super(message);
    this.http_status = http_status;
    this.uri = uri;
  }
  HTTPException(String message,Throwable cause,int http_status,String uri) {
    super(message,cause);
    this.http_status = http_status;
    this.uri = uri;
  }
  int getHttpStatus() {
    return http_status;
  }
  String getUri() {
    return uri;
  }
}


