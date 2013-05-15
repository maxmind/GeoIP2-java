package com.maxmind.geoip2.exception;

public class HTTPException extends GenericException {
  private int http_status;
  private String uri;

  public HTTPException(String message,int http_status,String uri) {
    super(message);
    this.http_status = http_status;
    this.uri = uri;
  }
  public HTTPException(String message,Throwable cause,int http_status,String uri) {
    super(message,cause);
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


