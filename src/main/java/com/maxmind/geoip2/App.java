package com.maxmind.geoip2;

import java.lang.*;
import java.util.*;
import java.io.*;
import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.apache.http.impl.auth.*;
import org.apache.http.auth.*;
import org.json.*;
/**
 * Hello world!
 *
 */
public class App 
{
  public static void main( String[] args )
  {
    try {
      String user_id = args[0];
      String license_key = args[1];
      String ip_address = args[2];
      Client cl = new Client(user_id,license_key);
      JSONObject o = cl.Country(ip_address);
      o = o.getJSONObject("country");
      o = o.getJSONObject("name");
      String name = o.getString("en");
      System.out.println(name);
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }
}


