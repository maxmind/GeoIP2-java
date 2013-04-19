package com.maxmind.geoip2;

public class GenericException extends RuntimeException {
  GenericException(String message) {
    super(message);
  }
  GenericException(String message,Throwable cause) {
    super(message,cause);
  }
}

