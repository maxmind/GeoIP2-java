package com.maxmind.geoip2;

import java.lang.*;
import java.util.*;
import java.io.*;
import org.json.*;

public class TraitsRecord
{
  private String ipAddress;
  private Integer autonomousSystemNumber;
  private String autonomousSystemOrganization;
  private String domain;
  private String isp;
  private String organization;
  private String userType;
  
  public TraitsRecord(JSONObject json) throws JSONException {
    ipAddress = json.getString("ip_address");
    if (json.has("autonomous_system_number")) {
      autonomousSystemNumber = new Integer(json.getInt("autonomous_system_number"));
    }
    domain = json.optString("domain",null);
    isp = json.optString("isp",null);
    organization = json.optString("organization",null);
    userType = json.optString("user_type",null);
  }
  public String getIpAddress() {
    return ipAddress;
  }
  public Integer getAutonomousSystemNumber() {
    return autonomousSystemNumber;
  }
  public String getAutonomousSystemOrganization() {
    return autonomousSystemOrganization;
  }
  public String getDomain() {
    return domain;
  }
  public String getIsp() {
    return isp;
  }
  public String getOrganization() {
    return organization;
  }
  public String getUserType() {
    return userType;
  }
}


