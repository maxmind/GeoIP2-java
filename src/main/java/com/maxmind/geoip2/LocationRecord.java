package com.maxmind.geoip2;

import java.lang.*;
import java.util.*;
import java.io.*;
import org.json.*;

public class LocationRecord
{
  private double longitude;
  private double latitude;
  private String postalCode;
  private String timeZone;
  private int metroCode;
  LocationRecord(JSONObject json) {
    try {
      postalCode = json.getString("postal_code");
      timeZone = json.getString("time_zone");
      longitude = json.getDouble("longitude");
      latitude = json.getDouble("latitude");
      metroCode = json.getInt("metro_code");
    } catch (JSONException e) {
      e.printStackTrace();
    }  
  }
  public String getPostalCode() {
    return postalCode;
  }
  public String getTimeZone() {
    return timeZone;
  }
  public double getLongitude() {
    return longitude;
  }
  public double getLatitude() {
    return latitude;
  }
  public int getMetroCode() {
    return metroCode;
  }
}


