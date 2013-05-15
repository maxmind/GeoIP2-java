package com.maxmind.geoip2.record;

import java.lang.*;
import java.util.*;
import java.io.*;
import org.json.*;

public class Country extends RecordWithNames
{
  private String isoCode;
  public Country(JSONObject jcountry) throws JSONException {
    super(jcountry);
    isoCode = jcountry.getString("iso_code");
  }
  public Country() {
    super();
  }
  public String getCode() {
    return isoCode;
  }
}

