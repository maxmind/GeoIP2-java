package com.maxmind.geoip2;

import java.lang.*;
import java.util.*;
import java.io.*;
import org.json.*;

public class TraitsRecord
{
  private String ipAddress;
  public TraitsRecord(JSONObject json) throws JSONException {
    ipAddress = json.getString("ip_address");
  }
  public String getIpAddress() {
    return ipAddress;
  }
}

