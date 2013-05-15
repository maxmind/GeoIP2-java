package com.maxmind.geoip2.record;

import java.lang.*;
import java.util.*;
import java.io.*;
import org.json.*;

public class City extends RecordWithNames
{
  public City(JSONObject jcountry) throws JSONException {
    super(jcountry);
  }
  public City() {
    super();
  }
}
