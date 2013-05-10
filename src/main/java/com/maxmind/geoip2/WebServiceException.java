package com.maxmind.geoip2;

public class WebServiceException extends HTTPException {
  private String code;
  WebServiceException(String message,String code,int http_status,String uri) {
    super(message,http_status,uri);
    this.code = code;
  }
  public String getCode() {
    return code;
  }
}

