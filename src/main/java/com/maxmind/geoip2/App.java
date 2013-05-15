package com.maxmind.geoip2;

import com.maxmind.geoip2.exception.*;
import com.maxmind.geoip2.model.*;
import com.maxmind.geoip2.webservice.*;
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
      Client client = new Client(user_id,license_key);
      Country country = null;
      try {
        country = client.Country(ip_address);
        System.out.println(country.getCountry().getName("en"));
      } catch (GenericException e) {
        System.out.println(e.getMessage());
      }
  }
}


