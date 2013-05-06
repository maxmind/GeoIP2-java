package com.maxmind.geoip2;

import java.lang.*;
import java.util.*;
import java.io.*;
import org.json.*;

public class CountryRecord extends RecordWithNames
{
  private String isoCode;
  public CountryRecord(JSONObject jcountry) throws JSONException {
    super(jcountry);
    isoCode = jcountry.getString("iso_code");
  }
  public CountryRecord() {
    super();
  }
  public String getCode() {
    return isoCode;
  }
}

