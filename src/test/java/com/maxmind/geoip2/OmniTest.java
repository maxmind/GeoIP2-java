package com.maxmind.geoip2;

import java.lang.*;
import java.util.*;
import java.io.*;
import org.json.*;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class OmniTest 
    extends TestCase
{
    Omni city;
    public OmniTest( String testName )
    {
        super( testName );
        JSONObject jcity = createJSONCity();
        city = new Omni(jcity);

    }
    public static Test suite()
    {
        return new TestSuite( OmniTest.class );
    }
    private JSONObject createJSONCity() {
      StringBuilder sb = new StringBuilder();
      sb.append("{");
      sb.append("\"city\":{");
      sb.append("\"confidence\":76,"); 
      sb.append("\"geoname_id\":9876,"); 
      sb.append("\"names\":{"); 
      sb.append("\"en\":\"Minneapolis\""); 
      sb.append("}");     
      sb.append("},");     

      sb.append("\"continent\":{");
      sb.append("\"continent_code\":\"NA\","); 
      sb.append("\"geoname_id\":42,"); 
      sb.append("\"names\":{"); 
      sb.append("\"en\":\"North America\""); 
      sb.append("}");     
      sb.append("},");     

      sb.append("\"country\":{");
      sb.append("\"confidence\":99,"); 
      sb.append("\"iso_code\":\"US\","); 
      sb.append("\"geoname_id\":1,"); 
      sb.append("\"names\":{"); 
      sb.append("\"en\":\"United States of America\""); 
      sb.append("}");     
      sb.append("},");     

      sb.append("\"location\":{");
      sb.append("\"accuracy_radius\":1500,"); 
      sb.append("\"latitude\":44.98,");
      sb.append("\"longitude\":93.2636,");
      sb.append("\"metro_code\":765,");
      sb.append("\"postal_code\":\"55401\",");
      sb.append("\"postal_confidence\":33,");
      sb.append("\"time_zone\":\"America/Chicago\"");
      sb.append("},");
      sb.append("\"registered_country\":{");
      sb.append("\"geoname_id\":2,");
      sb.append("\"iso_code\":\"CA\",");
      sb.append("\"names\":{"); 
      sb.append("\"en\":\"Canada\""); 
      sb.append("}");     
      sb.append("},"); 
      sb.append("\"represented_country\":{");
      sb.append("\"geoname_id\":3,");
      sb.append("\"iso_code\":\"GB\",");
      sb.append("\"names\":{"); 
      sb.append("\"en\":\"United Kingdom\""); 
      sb.append("},");     
      sb.append("\"type\":\"C<military>\"");
      sb.append("},"); 
      sb.append("\"subdivisions\":[{");
      sb.append("\"confidence\":88,"); 
      sb.append("\"geoname_id\":574635,"); 
      sb.append("\"iso_code\":\"MN\","); 
      sb.append("\"names\":{"); 
      sb.append("\"en\":\"Minnesota\""); 
      sb.append("}");     
      sb.append("}");     
      sb.append("],");     
      sb.append("\"traits\":{");
      sb.append("\"autonomous_system_number\":1234,");
      sb.append("\"autonomous_system_organization\":\"AS Organization\",");
      sb.append("\"domain\":\"example.com\",");
      sb.append("\"ip_address\":\"1.2.3.4\",");
      sb.append("\"is_anonymous_proxy\":true,");
      sb.append("\"isp\":\"Comcast\",");
      sb.append("\"organization\":\"Blorg\",");
      sb.append("\"user_type\":\"college\"");
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
    public void testSubdivisionsList() {
      ArrayList<Subdivision> subdivisionsList = city.getSubdivisionsList();
      assertNotNull("city.getSubdivisionsList returns null",subdivisionsList);
      if (subdivisionsList.size() == 0) {
        fail("subdivisionsList is empty");
      }
      Subdivision subdivision = subdivisionsList.get(0);
      assertEquals(
        "subdivision.getConfidence() does not return 88",
         new Integer(88),
         subdivision.getConfidence()
      );
      assertEquals(
        "subdivision.getGeoNameId() does not return 574635",
         574635,
         subdivision.getGeoNameId()
      );
      assertEquals(
        "subdivision.getCode() does not return MN",
        "MN",
        subdivision.getCode()
      );
    }
    public void testTraits() {
      TraitsRecord traits = city.getTraits();

      assertNotNull("city.getTraits() returns null",traits);
      assertEquals(
        "traits.getAutonomousSystemNumber() does not return 1234",
         new Integer(1234),
         traits.getAutonomousSystemNumber()
      );
      assertEquals(
        "traits.getAutonomousSystemOrganization() does not return AS Organization",
         "AS Organization",
         traits.getAutonomousSystemOrganization()
      );
      assertEquals(
        "traits.getAutonomousSystemOrganization() does not return example.com",
         "example.com",
         traits.getDomain()
      );
      assertEquals(
        "traits.getIpAddress() does not return 1.2.3.4",
         "1.2.3.4",
         traits.getIpAddress()
      );
      assertEquals(
        "traits.isAnonymousProxy() returns false",
         true,
         traits.isAnonymousProxy()
      );
      assertEquals(
        "traits.getIsp() does not return Comcast",
         "Comcast",
         traits.getIsp()
      );
      assertEquals(
        "traits.getOrganization() does not return Blorg",
         "Blorg",
         traits.getOrganization()
      );
      assertEquals(
        "traits.getUserType() does not return userType",
         "college",
         traits.getUserType()
      );
    }
    public void testLocation() {

      LocationRecord location = city.getLocation();

      assertNotNull("city.getLocation() returns null",location);

      assertEquals(
        "location.getAccuracyRadius() does not return 1500",
         new Integer(1500),
         location.getAccuracyRadius()
      );

      double latitude = location.getLatitude();
      assertEquals(
        "location.getLatitude() does not return 44.98",44.98,
        latitude,
        0.1
      );
      double longitude = location.getLongitude();
      assertEquals(
        "location.getLongitude() does not return 93.2636",93.2636,
        longitude,
        0.1
      );
      assertEquals(
        "location.getMetroCode() does not return 765",
         new Integer(765),
         location.getMetroCode()
       );


      assertEquals(
        "location.getPostalCode() does not return 55401",
         "55401",
         location.getPostalCode()
      );
      assertEquals(
        "location.getPostalConfidence() does not return 33",
         new Integer(33),
         location.getPostalConfidence()
      );
      assertEquals(
        "location.getTimeZone() does not return America/Chicago",
         "America/Chicago",
         location.getTimeZone()
      );
    }
    public void testRepresentedCountry() {
      assertNotNull("city.getRepresentedCountry() returns null",
         city.getRepresentedCountry());

      assertEquals(
        "city.getRepresentedCountry().getType() does not return C<military>", 
        "C<military>",
         city.getRepresentedCountry().getType());
   }
}

