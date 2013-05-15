package com.maxmind.geoip2.record;

import java.lang.*;
import java.util.*;
import java.io.*;
import org.json.*;

public class Subdivision extends RecordWithNames
{
  private String isoCode;
  private HashMap<String,String> names;
  public Subdivision(JSONObject jcountry) throws JSONException {
    super(jcountry);
    isoCode = jcountry.getString("iso_code");
  }
  public String getCode() {
    return isoCode;
  }
}

