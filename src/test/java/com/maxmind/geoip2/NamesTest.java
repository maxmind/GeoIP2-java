package com.maxmind.geoip2;

import java.lang.*;
import java.util.*;
import java.io.*;
import org.json.*;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class NamesTest 
    extends TestCase
{
    public NamesTest( String testName )
    {
        super( testName );
    }

    public static Test suite()
    {
        return new TestSuite( NamesTest.class );
    }
    private JSONObject createJSONCountry() {
      StringBuilder sb = new StringBuilder();
      sb.append("{\"continent\":{");
      sb.append("\"continent_code\":\"NA\",");
      sb.append("\"geoname_id\":42,");
      sb.append("\"names\":{");
      sb.append("\"en\":\"North America\",");
      sb.append("\"zh-CN\":\"北美洲\"");
      sb.append("}");
      sb.append("},");
      sb.append("\"country\":{");
      sb.append("\"geoname_id\":1,");
      sb.append("\"iso_code\":\"US\",");
      sb.append("\"confidence\":56,");
      sb.append("\"names\":{");
      sb.append("\"en\":\"United States\",");
      sb.append("\"ru\":\"объединяет государства\",");
      sb.append("\"zh-CN\":\"美国\"");
      sb.append("}");
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
    public void testNames() {
      JSONObject jcountry = createJSONCountry();
      Country country = new Country(jcountry);
      assertEquals(
"country.getContinent().getName(\"zh-CN\") does not return 北美洲",
         "北美洲",
         country.getContinent().getName("zh-CN")
      );
      assertEquals(
"country.getCountry().getName(\"ru\") does not return объединяет государства",
         "объединяет государства",
         country.getCountry().getName("ru")
       );
      assertEquals(
"country.getCountry().getName(\"zh-CN\") does not return 美国",
         "美国",
         country.getCountry().getName("zh-CN")
       );
    }
}

