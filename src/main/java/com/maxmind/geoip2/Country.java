package com.maxmind.geoip2;


import java.lang.*;
import java.util.*;
import java.io.*;
import org.json.*;

//import record.Country;
//import record.Continent;
public class Country
{
  private com.maxmind.geoip2.record.Country country;
  private com.maxmind.geoip2.record.Country registered_country;
  private com.maxmind.geoip2.record.Continent continent;
  Country(JSONObject json) {
    try {
      JSONObject jcontinent = json.getJSONObject("continent");
      continent = new com.maxmind.geoip2.record.Continent(jcontinent);
      JSONObject jcountry = json.getJSONObject("country");
      country = new com.maxmind.geoip2.record.Country(jcountry);
      jcountry = json.getJSONObject("registered_country");
      registered_country = new com.maxmind.geoip2.record.Country(jcountry);
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }
  public com.maxmind.geoip2.record.Country get_country() {
    return country;
  }
  public com.maxmind.geoip2.record.Country get_registered_country() {
    return registered_country;
  }
  public com.maxmind.geoip2.record.Continent get_continent() {
    return continent;
  }
}

