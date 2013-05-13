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
  private CountryRecord registeredCountry;
  private ContinentRecord continent;
  private TraitsRecord traits;
  Country(JSONObject json) {
    try {
      if (json.has("continent")) {
        JSONObject jcontinent = json.getJSONObject("continent");
        continent = new ContinentRecord(jcontinent);
      } else {
        continent = new ContinentRecord();
      }
      if (json.has("country")) {
        JSONObject jcountry = json.getJSONObject("country");
        country = new CountryRecord(jcountry);
      } else {
        country = new CountryRecord();
      }
      if (json.has("registered_country")) {
        JSONObject jcountry = json.getJSONObject("registered_country");
        registeredCountry = new CountryRecord(jcountry);
      } else {
        registeredCountry = new CountryRecord();
      }
      JSONObject jtraits = json.getJSONObject("traits");
      traits = new TraitsRecord(jtraits);
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }
  public CountryRecord getCountry() {
    return country;
  }
  public CountryRecord getRegisteredCountry() {
    return registeredCountry;
  }
  public ContinentRecord getContinent() {
    return continent;
  }
  public TraitsRecord getTraits() {
    return traits;
  }
}

