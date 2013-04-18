package com.maxmind.geoip2;

public class HTTPException extends RuntimeException {
  private int http_status;
  private String uri;

  HTTPException(String message,int http_status,String uri) {
    super(message);
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


