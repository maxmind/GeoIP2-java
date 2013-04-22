package com.maxmind.geoip2;

import java.lang.*;
import java.util.*;
import java.io.*;
import org.json.*;

public class CityRecord extends RecordWithNames
{
  public CityRecord(JSONObject jcountry) throws JSONException {
    super(jcountry);
  }
}
