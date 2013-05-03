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
  private boolean anonymousProxy;
  private boolean transparentProxy;
  private boolean usMilitary;
  
  public TraitsRecord(JSONObject json) throws JSONException {
    ipAddress = json.getString("ip_address");
    if (json.has("autonomous_system_number")) {
      autonomousSystemNumber = new Integer(json.getInt("autonomous_system_number"));
    }
    autonomousSystemOrganization = json.optString("autonomous_system_organization",null);
    domain = json.optString("domain",null);
    isp = json.optString("isp",null);
    organization = json.optString("organization",null);
    userType = json.optString("user_type",null);
    anonymousProxy = json.optBoolean("is_anonymous_proxy");
    transparentProxy = json.optBoolean("is_transparent_proxy");
    usMilitary = json.optBoolean("is_us_military");

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
  public boolean isAnonymousProxy() {
    return anonymousProxy;
  }
  public boolean isTransparentProxy() {
    return transparentProxy;
  }
  public boolean isUsMilitary() {
    return usMilitary;
  }

}


