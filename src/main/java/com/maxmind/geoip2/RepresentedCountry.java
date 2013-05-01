package com.maxmind.geoip2;

import java.lang.*;
import java.util.*;
import java.io.*;
import org.json.*;

public class RepresentedCountry extends CountryRecord
{
  private String type;
  public RepresentedCountry(JSONObject jcountry) throws JSONException {
    super(jcountry);
    type = jcountry.getString("type");
  }
  String getType() {
    return type;
  }
}

