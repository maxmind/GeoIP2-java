package com.maxmind.geoip2;

import java.lang.*;
import java.util.*;
import java.io.*;
import org.json.*;

public class CountryRecord extends RecordWithNames
{
  private String isoCode;
  private HashMap<String,String> names;
  private int geonameId;
  public CountryRecord(JSONObject jcountry) throws JSONException {
    super(jcountry);
    geonameId = jcountry.getInt("geoname_id");
    isoCode = jcountry.getString("iso_code");
  }
  public String getCode() {
    return isoCode;
  }
  public int getGeonameId() {
    return geonameId;
  }
}

