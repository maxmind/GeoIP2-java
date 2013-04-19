package com.maxmind.geoip2;


import java.lang.*;
import java.util.*;
import java.io.*;
import org.json.*;

//import record.Country;
//import record.Continent;
public class Country
{
  private CountryRecord country;
  private CountryRecord registered_country;
  private ContinentRecord continent;
  private TraitsRecord traits;
  Country(JSONObject json) {
    try {
      JSONObject jcontinent = json.getJSONObject("continent");
      continent = new ContinentRecord(jcontinent);
      JSONObject jcountry = json.getJSONObject("country");
      country = new CountryRecord(jcountry);
      jcountry = json.getJSONObject("registered_country");
      registered_country = new CountryRecord(jcountry);
      JSONObject jtraits = json.getJSONObject("traits");
      traits = new TraitsRecord(jtraits);
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }
  public CountryRecord get_country() {
    return country;
  }
  public CountryRecord get_registered_country() {
    return registered_country;
  }
  public ContinentRecord get_continent() {
    return continent;
  }
  public TraitsRecord get_traits() {
    return traits;
  }
}

