package com.maxmind.geoip2;

import java.lang.*;
import java.util.*;
import java.io.*;
import org.json.*;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class CountryTest 
    extends TestCase
{

    public CountryTest( String testName )
    {
        super( testName );
    }
    public static Test suite()
    {
        return new TestSuite( CountryTest.class );
    }
    private JSONObject createJSONCountry() {

      StringBuilder sb = new StringBuilder();
      sb.append("{\"continent\":{");
      sb.append("\"continent_code\":\"NA\",");
      sb.append("\"geoname_id\":42,");
      sb.append("\"names\":{\"en\":\"North America\"}");
      sb.append("},");
      sb.append("\"country\":{");
      sb.append("\"geoname_id\":1,");
      sb.append("\"iso_code\":\"US\",");
      sb.append("\"names\":{\"en\":\"United States\"}");
      sb.append("},");
      sb.append("\"registered_country\":{");
      sb.append("\"geoname_id\":2,");
      sb.append("\"iso_code\":\"CA\",");
      sb.append("\"names\":{\"en\":\"Canada\"}");
      sb.append("},");
      sb.append("\"traits\":{");
      sb.append("\"ip_address\":\"1.2.3.4\",");
      sb.append("}}");
      String str = sb.toString();
      try {
        return new JSONObject(str);
      } catch (JSONException e) {
        fail(e.getMessage());
        return null;
      }

    }
    
    public void testCountry() {
      JSONObject jcountry = createJSONCountry();
      Country country = new Country(jcountry);
      assertEquals(
        "country.getContinent().getCode() does not return NA",
         country.getContinent().getCode(),
         "NA");
      assertEquals(
        "country.getContinent().getGeoNameId() does not return 42",
         country.getContinent().getGeoNameId(),
         42);
      assertEquals(
"country.getContinent().getName(\"en\") does not return North America",
         country.getContinent().getName("en"),
         "North America");

      assertEquals(
        "country.getCountry().getCode() does not return US",
         country.getCountry().getCode(),
         "US");
      assertEquals(
        "country.getCountry().getGeoNameId() does not return 1",
         country.getCountry().getGeoNameId(),
         1);
      assertEquals(
"country.getCountry().getName(\"en\") does not return United States",
         country.getCountry().getName("en"),
         "United States");

      assertEquals(
        "country.getRegisteredCountry().getCode() does not return CA",
         country.getRegisteredCountry().getCode(),
         "CA");
      assertEquals(
        "country.getRegisteredCountry().getGeoNameId() does not return 2",
         country.getRegisteredCountry().getGeoNameId(),
         2);
      assertEquals(
"country.getRegisteredCountry().getName(\"en\") does not return United States",
         country.getRegisteredCountry().getName("en"),
         "Canada");
      assertEquals(
        "country.getTraits().getIpAddress does not return 1.2.3.4",
         country.getTraits().getIpAddress(),
         "1.2.3.4");

    }

}

