package com.maxmind.geoip2.model;


import java.lang.*;
import java.util.*;
import java.io.*;
import org.json.*;
import com.maxmind.geoip2.record.*;

public class Country
{
  private com.maxmind.geoip2.record.Country country;
  private com.maxmind.geoip2.record.Country registeredCountry;
  private Continent continent;
  private Traits traits;
  public Country(JSONObject json) {
    try {
      if (json.has("continent")) {
        JSONObject jcontinent = json.getJSONObject("continent");
        continent = new Continent(jcontinent);
      } else {
        continent = new Continent();
      }
      if (json.has("country")) {
        JSONObject jcountry = json.getJSONObject("country");
        country = new com.maxmind.geoip2.record.Country(jcountry);
      } else {
        country = new com.maxmind.geoip2.record.Country();
      }
      if (json.has("registered_country")) {
        JSONObject jcountry = json.getJSONObject("registered_country");
        registeredCountry = new com.maxmind.geoip2.record.Country(jcountry);
      } else {
        registeredCountry = new com.maxmind.geoip2.record.Country();
      }
      JSONObject jtraits = json.getJSONObject("traits");
      traits = new Traits(jtraits);
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }
  public com.maxmind.geoip2.record.Country getCountry() {
    return country;
  }
  public com.maxmind.geoip2.record.Country getRegisteredCountry() {
    return registeredCountry;
  }
  public Continent getContinent() {
    return continent;
  }
  public Traits getTraits() {
    return traits;
  }
}
