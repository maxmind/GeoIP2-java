package com.maxmind.geoip2;

import java.lang.*;
import java.util.*;
import java.io.*;
import org.json.*;

public class ContinentRecord extends RecordWithNames
{
  private String continentCode;
  public ContinentRecord(JSONObject jcontinent) throws JSONException {
    super(jcontinent);
    continentCode = jcontinent.getString("continent_code");
  }
  public ContinentRecord() {
    super();
  }
  public String getCode() {
    return continentCode;
  }
}


