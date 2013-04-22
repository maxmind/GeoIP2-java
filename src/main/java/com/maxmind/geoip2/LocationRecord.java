package com.maxmind.geoip2;

import java.lang.*;
import java.util.*;
import java.io.*;
import org.json.*;

public class LocationRecord
{
  double longitude;
  double latitude;
  String postalCode;
  String timeZone;
  int metroCode;
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
  String getPostalCode() {
    return postalCode;
  }
  String getTimeZone() {
    return timeZone;
  }
  double getLongitude() {
    return longitude;
  }
  double getLatitude() {
    return latitude;
  }
  int getMetroCode() {
    return metroCode;
  }
}


