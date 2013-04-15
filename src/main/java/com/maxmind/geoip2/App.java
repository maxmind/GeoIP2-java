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

public class App 
{
  public static void main( String[] args )
  {
      String user_id = args[0];
      String license_key = args[1];
      String ip_address = args[2];
      Client cl = new Client(user_id,license_key);
      Country c = cl.Country(ip_address);
      System.out.println(c.get_country_name("en"));
  }
}


