package com.maxmind.geoip2;

import java.lang.*;
import java.util.*;
import java.io.*;
import org.json.*;

public abstract class RecordWithNames {

  private HashMap<String,String> names;
  private int geoNameId;
  private Integer confidence;
  protected RecordWithNames(JSONObject json) throws JSONException {
    geoNameId = json.getInt("geoname_id");
    JSONObject jnames = json.getJSONObject("names");
    names = new HashMap<String,String>();
    for (Iterator<String> i = jnames.keys(); i.hasNext();) {
      String key = i.next();
      String value = jnames.getString(key);
      names.put(key,value);
    }        
    if (json.has("confidence")) {
      confidence = new Integer(json.getInt("confidence"));
    }
  }
  public String getName(String l) {
    return names.get(l);
  }
  public int getGeoNameId() {
    return geoNameId;
  }
  public Integer getConfidence() {
    return confidence;
  }
}

