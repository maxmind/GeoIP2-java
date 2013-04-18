package com.maxmind.geoip2;

import java.lang.*;
import java.util.*;
import java.io.*;
import org.json.*;

public class RecordWithNames {

  private HashMap<String,String> names;
  public RecordWithNames(JSONObject json) throws JSONException {
    JSONObject jnames = json.getJSONObject("names");
    names = new HashMap<String,String>();
    for (Iterator<String> i = jnames.keys(); i.hasNext();) {
      String key = i.next();
      String value = jnames.getString(key);
      names.put(key,value);
    }        
  }
  public String getName(String l) {
    return names.get(l);
  }

}

