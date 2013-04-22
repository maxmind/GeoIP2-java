package com.maxmind.geoip2;


import java.lang.*;
import java.util.*;
import java.io.*;
import org.json.*;

public class City extends Country
{
  private CityRecord city;
  private LocationRecord location;
  private RegionRecord region;
  City(JSONObject json) {
    super(json);
    try {
      JSONObject jcity = json.getJSONObject("city");
      city = new CityRecord(jcity);      
      JSONObject jlocation = json.getJSONObject("location");
      location = new LocationRecord(jlocation);
      JSONObject jregion = json.getJSONObject("region");
      region = new RegionRecord(jregion);
    } catch (JSONException e) {
      e.printStackTrace();
    }  
  }
  public CityRecord getCity() {
    return city;
  }
  public LocationRecord getLocation() {
    return location;
  }
  public RegionRecord getRegion() {
    return region;
  } 
}

