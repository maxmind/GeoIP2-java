package com.maxmind.geoip2.record;

import java.lang.*;
import java.util.*;
import java.io.*;
import org.json.*;

public class RepresentedCountry extends Country
{
  private String type;
  public RepresentedCountry(JSONObject jcountry) throws JSONException {
    super(jcountry);
    type = jcountry.getString("type");
  }
  public RepresentedCountry() {
    super();
    type = "";
  }
  public String getType() {
    return type;
  }
}
