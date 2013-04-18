package com.maxmind.geoip2;

import java.lang.*;
import java.util.*;
import java.io.*;
import org.json.*;

public class ContinentRecord extends RecordWithNames
{
  private String continentCode;
  private int geoNameId;
  public ContinentRecord(JSONObject jcontinent) throws JSONException {
    super(jcontinent);
    continentCode = jcontinent.getString("continent_code");
    geoNameId = jcontinent.getInt("geoname_id");
  }
  public String getCode() {
    return continentCode;
  }
  public int getGeonameId() {
    return geoNameId;
  }
}


