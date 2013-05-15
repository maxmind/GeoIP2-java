package com.maxmind.geoip2.model;

import org.json.*;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class NamesTest extends TestCase {
    public NamesTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(NamesTest.class);
    }

    private JSONObject createJSONCountry() {
        String str = "{\"continent\":{" + "\"continent_code\":\"NA\","
                + "\"geoname_id\":42," + "\"names\":{"
                + "\"en\":\"North America\"," + "\"zh-CN\":\"北美洲\"" + "}"
                + "}," + "\"country\":{" + "\"geoname_id\":1,"
                + "\"iso_code\":\"US\"," + "\"confidence\":56," + "\"names\":{"
                + "\"en\":\"United States\","
                + "\"ru\":\"объединяет государства\"," + "\"zh-CN\":\"美国\""
                + "}" + "}," + "\"registered_country\":{" + "\"geoname_id\":2,"
                + "\"iso_code\":\"CA\"," + "\"names\":{\"en\":\"Canada\"}"
                + "}," + "\"traits\":{" + "\"ip_address\":\"1.2.3.4\"," + "}}";

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
                "北美洲", country.getContinent().getName("zh-CN"));
        assertEquals(
                "country.getCountry().getName(\"ru\") does not return объединяет государства",
                "объединяет государства", country.getCountry().getName("ru"));
        assertEquals(
                "country.getCountry().getName(\"zh-CN\") does not return 美国",
                "美国", country.getCountry().getName("zh-CN"));
    }
}
