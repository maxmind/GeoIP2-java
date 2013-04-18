package com.maxmind.geoip2;

import java.lang.*;
import java.util.*;
import java.io.*;
import org.json.*;

public class Country
{
  private String continent_code;
  private int continent_geoname_id;
  private HashMap<String,String> continent_name;

  private String iso_code;
  private int country_geoname_id;
  private HashMap<String,String> country_name;

  private String registered_iso_code;
  private int registered_country_geoname_id;
  private HashMap<String,String> registered_country_name;
  
  Country(JSONObject json) {
    try {
      JSONObject jcontinent = json.getJSONObject("continent");
      String continent_code = jcontinent.getString("continent_code");
      int continent_geoname_id = jcontinent.getInt("geoname_id");
      JSONObject jnames = jcontinent.getJSONObject("names");
      continent_name = new HashMap<String,String>();
      for (Iterator<String> i = jnames.keys(); i.hasNext();) {
        String key = i.next();
        String value = jnames.getString(key);
        continent_name.put(key,value);
      }
      JSONObject jcountry = json.getJSONObject("country");
      country_geoname_id = jcountry.getInt("geoname_id");
      iso_code = jcountry.getString("iso_code");
      jnames = jcountry.getJSONObject("names");
      country_name = new HashMap<String,String>();
      for (Iterator<String> i = jnames.keys(); i.hasNext();) {
        String key = i.next();
        String value = jnames.getString(key);
        country_name.put(key,value);
      }
      jcountry = json.getJSONObject("registered_country");
      registered_country_geoname_id = jcountry.getInt("geoname_id");
      registered_iso_code = jcountry.getString("iso_code");
      jnames = jcountry.getJSONObject("names");
      registered_country_name = new HashMap<String,String>();
      for (Iterator<String> i = jnames.keys(); i.hasNext();) {
        String key = i.next();
        String value = jnames.getString(key);
        registered_country_name.put(key,value);
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }
  
  String get_continent_code() {
    return continent_code;
  }
  int get_continent_geoname_id() {
    return continent_geoname_id;
  }
  String get_continent_name(String l) {
    return continent_name.get(l);
  }
  String get_registered_country_name(String l) {
    return registered_country_name.get(l);
  }
  String get_country_name(String l) {
    return country_name.get(l);
  }
  int get_country_geoname_id() {
    return country_geoname_id;
  }
  String get_iso_code() {
    return iso_code;
  }
  String get_registered_iso_code() {
    return registered_iso_code;
  }

}

