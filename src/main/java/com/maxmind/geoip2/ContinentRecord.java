package com.maxmind.geoip2;

import java.lang.*;
import java.util.*;
import java.io.*;
import org.json.*;

public class ContinentRecord
{
  private String continentCode;
  private HashMap<String,String> names;
  private int geoNameId;
  public ContinentRecord(JSONObject jcontinent) throws JSONException {
    continentCode = jcontinent.getString("continent_code");
    geoNameId = jcontinent.getInt("geoname_id");
    JSONObject jnames = jcontinent.getJSONObject("names");
    names = new HashMap<String,String>();
    for (Iterator<String> i = jnames.keys(); i.hasNext();) {
      String key = i.next();
      String value = jnames.getString(key);
      names.put(key,value);
    }        
  }
  public String getCode() {
    return continentCode;
  }
  public int getGeonameId() {
    return geoNameId;
  }
  public String getName(String l) {
    return names.get(l);
  }
}


