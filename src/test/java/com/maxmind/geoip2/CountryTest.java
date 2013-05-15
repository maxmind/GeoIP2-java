package com.maxmind.geoip2;

import com.maxmind.geoip2.model.*;
import org.json.*;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class CountryTest extends TestCase {

    public CountryTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(CountryTest.class);
    }

    private JSONObject createJSONCountry() {
        String str = "{\"continent\":{" + "\"continent_code\":\"NA\","
                + "\"geoname_id\":42," + "\"names\":{\"en\":\"North America\"}"
                + "}," + "\"country\":{" + "\"geoname_id\":1,"
                + "\"iso_code\":\"US\"," + "\"confidence\":56,"
                + "\"names\":{\"en\":\"United States\"}" + "},"
                + "\"registered_country\":{" + "\"geoname_id\":2,"
                + "\"iso_code\":\"CA\"," + "\"names\":{\"en\":\"Canada\"}"
                + "}," + "\"traits\":{" + "\"ip_address\":\"1.2.3.4\"," + "}}";
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
        assertEquals("country.getContinent().getCode() does not return NA",
                "NA", country.getContinent().getCode());
        assertEquals(
                "country.getContinent().getGeoNameId() does not return 42", 42,
                (int) country.getContinent().getGeoNameId());
        assertEquals(
                "country.getContinent().getName(\"en\") does not return North America",
                "North America", country.getContinent().getName("en"));
        assertEquals("country.getCountry().getCode() does not return US", "US",
                country.getCountry().getIsoCode());
        assertEquals("country.getCountry().getGeoNameId() does not return 1",
                1, (int) country.getCountry().getGeoNameId());
        assertEquals("country.getCountry().getConfidence() does not return 56",
                new Integer(56), country.getCountry().getConfidence());
        assertEquals(
                "country.getCountry().getName(\"en\") does not return United States",
                "United States", country.getCountry().getName("en"));

        assertEquals(
                "country.getRegisteredCountry().getCode() does not return CA",
                "CA", country.getRegisteredCountry().getIsoCode());
        assertEquals(
                "country.getRegisteredCountry().getGeoNameId() does not return 2",
                2, (int) country.getRegisteredCountry().getGeoNameId());
        assertEquals(
                "country.getRegisteredCountry().getName(\"en\") does not return United States",
                "Canada", country.getRegisteredCountry().getName("en"));
        assertEquals(
                "country.getTraits().getIpAddress does not return 1.2.3.4",
                "1.2.3.4", country.getTraits().getIpAddress());

    }

}
