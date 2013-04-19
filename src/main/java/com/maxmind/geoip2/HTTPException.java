package com.maxmind.geoip2;

public class HTTPException extends GenericException {
  private int http_status;
  private String uri;

  HTTPException(String message,int http_status,String uri) {
    super(message);
    http_status = this.http_status;
    uri = this.uri;
  }
  HTTPException(String message,Throwable cause,int http_status,String uri) {
    super(message,cause);
    http_status = this.http_status;
    uri = this.uri;
  }
  int get_http_status() {
    return http_status;
  }
  String get_uri() {
    return uri;
  }
}


