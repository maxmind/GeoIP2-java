package com.maxmind.geoip2;

import java.lang.*;
import java.util.*;
import java.io.*;
import org.json.*;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class CityTest 
    extends TestCase
{
    public CityTest( String testName )
    {
        super( testName );
    }
    public static Test suite()
    {
        return new TestSuite( CityTest.class );
    }
    private JSONObject createJSONCity() {
      StringBuilder sb = new StringBuilder();
      sb.append("{");
      sb.append("\"location\":{");
      sb.append("\"postal_code\":\"12345\",");
      sb.append("\"postal_confidence\":50,");
      sb.append("\"time_zone\":\"EST\",");
      sb.append("\"longitude\":42.0,");
      sb.append("\"latitude\":46.0,");
      sb.append("\"metro_code\":843,");
      sb.append("\"accuracy_radius\":20");
      sb.append("}");
      sb.append("\"represented_country\":{");
      sb.append("type:\"C<military>\"");
      sb.append("}");      
      sb.append("}");
      String str = sb.toString();
      try {
        return new JSONObject(str);
      } catch (JSONException e) {
        fail(e.getMessage());
        return null;
      }
    }
    public void testCity() {
      JSONObject jcity = createJSONCity();
      City city = new City(jcity);
      assertEquals(
        "city.getLocation().getPostalCode() does not return 12345",
         city.getLocation().getPostalCode(),
         "12345");
      assertEquals(
        "city.getLocation().getPostalConfidence() does not return 50",
         city.getLocation().getPostalConfidence(),
         new Integer(50));
      assertEquals(
        "city.getLocation().getTimeZone() does not return EST",
         city.getLocation().getTimeZone(),
         "EST");
      double longitude = city.getLocation().getLongitude();
      assertEquals(
        "city.getLocation().getLongitude() does not return 42.0",longitude,42.0,0.1);
      double latitude = city.getLocation().getLatitude();
      assertEquals(
        "city.getLocation().getLatitude() does not return 46.0",latitude,46.0,0.1);

      assertEquals(
        "city.getRepresentedCountry().getType() does not return C<military>", 
        city.getRepresentedCountry().getType(),"C<military>");
    }
}

