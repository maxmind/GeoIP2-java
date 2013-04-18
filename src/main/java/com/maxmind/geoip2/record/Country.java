package com.maxmind.geoip2.record;

import java.lang.*;
import java.util.*;
import java.io.*;
import org.json.*;

public class Country
{
  private String iso_code;
  private HashMap<String,String> names;
  private int geoname_id;
  public Country(JSONObject jcountry) throws JSONException {
    geoname_id = jcountry.getInt("geoname_id");
    iso_code = jcountry.getString("iso_code");
    JSONObject jnames = jcountry.getJSONObject("names");
    names = new HashMap<String,String>();
    for (Iterator<String> i = jnames.keys(); i.hasNext();) {
      String key = i.next();
      String value = jnames.getString(key);
      names.put(key,value);
    }    
  }
  public String get_code() {
    return iso_code;
  }
  public int get_geoname_id() {
    return geoname_id;
  }
  public String get_name(String l) {
    return names.get(l);
  }
}

