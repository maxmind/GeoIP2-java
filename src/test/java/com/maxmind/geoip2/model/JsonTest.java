package com.maxmind.geoip2.model;

import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jr.ob.JSON;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class JsonTest {

    @Test
    public void testInsightsSerialization() throws IOException {
        String json = JSON.std
                .composeString()
                .startObject()
                .startObjectField("maxmind")
                .put("queries_remaining", 11)
                .end()
                .startObjectField("registered_country")
                .put("geoname_id", 2)
                .startObjectField("names")
                .put("en", "Canada")
                .end()
                .put("iso_code", "CA")
                .end()
                .startObjectField("traits")
                .put("is_anonymous_proxy", true)
                .put("autonomous_system_number", 1234)
                .put("isp", "Comcast")
                .put("ip_address", "1.2.3.4")
                .put("is_satellite_provider", true)
                .put("autonomous_system_organization", "AS Organization")
                .put("user_type", "college")
                .put("organization", "Blorg")
                .put("domain", "example.com")
                // This is here just to simplify the testing. We expect the
                // difference
                .put("is_legitimate_proxy", false)
                .end()
                .startObjectField("country")
                .startObjectField("names")
                .put("en", "United States of America")
                .end()
                .put("geoname_id", 1)
                .put("iso_code", "US")
                .put("confidence", 99)
                .end()
                .startObjectField("continent")
                .startObjectField("names")
                .put("en", "North America")
                .end()
                .put("code", "NA")
                .put("geoname_id", 42)
                .end()
                .startObjectField("location")
                .put("average_income", 24626)
                .put("population_density", 1341)
                .put("time_zone", "America/Chicago")
                .put("accuracy_radius", 1500)
                .put("metro_code", 765)
                .put("latitude", 44.98)
                .put("longitude", 93.2636)
                .end()
                .startArrayField("subdivisions")
                .startObject()
                .put("confidence", 88)
                .put("iso_code", "MN")
                .put("geoname_id", 574635)
                .startObjectField("names")
                .put("en", "Minnesota")
                .end()
                .end()
                .startObject()
                .put("iso_code", "TT")
                .end()
                .end()
                .startObjectField("represented_country")
                .put("geoname_id", 3)
                .startObjectField("names")
                .put("en", "United Kingdom")
                .end()
                .put("type", "C<military>")
                .put("iso_code", "GB")
                .end()
                .startObjectField("postal")
                .put("code", "55401")
                .put("confidence", 33)
                .end()
                .startObjectField("city")
                .put("confidence", 76)
                .put("geoname_id", 9876)
                .startObjectField("names")
                .put("en", "Minneapolis")
                .end()
                .end()
                .end()
                .finish();

        testRoundTrip(InsightsResponse.class, json);
    }

    @Test
    public void testCitySerialization() throws IOException {
        String json = JSON.std
                .composeString()
                .startObject()
                .startObjectField("maxmind")
                .put("queries_remaining", 11)
                .end()
                .startObjectField("registered_country")
                .put("geoname_id", 2)
                .startObjectField("names")
                .put("en", "Canada")
                .end()
                .put("iso_code", "CA")
                .end()
                .startObjectField("traits")
                .put("is_anonymous_proxy", true)
                .put("autonomous_system_number", 1234)
                .put("isp", "Comcast")
                .put("ip_address", "1.2.3.4")
                .put("is_satellite_provider", true)
                .put("autonomous_system_organization", "AS Organization")
                .put("organization", "Blorg")
                .put("domain", "example.com")
                // This is here just to simplify the testing. We expect the
                // difference
                .put("is_legitimate_proxy", false)
                .end()
                .startObjectField("country")
                .startObjectField("names")
                .put("en", "United States of America")
                .end()
                .put("geoname_id", 1)
                .put("iso_code", "US")
                .end()
                .startObjectField("continent")
                .startObjectField("names")
                .put("en", "North America")
                .end()
                .put("code", "NA")
                .put("geoname_id", 42)
                .end()
                .startObjectField("location")
                .put("time_zone", "America/Chicago")
                .put("metro_code", 765)
                .put("latitude", 44.98)
                .put("longitude", 93.2636)
                .end()
                .startArrayField("subdivisions")
                .startObject()
                .put("iso_code", "MN")
                .put("geoname_id", 574635)
                .startObjectField("names")
                .put("en", "Minnesota")
                .end()
                .end()
                .startObject()
                .put("iso_code", "TT")
                .end()
                .end()
                .startObjectField("represented_country")
                .put("geoname_id", 3)
                .startObjectField("names")
                .put("en", "United Kingdom")
                .end()
                .put("type", "C<military>")
                .put("iso_code", "GB")
                .end()
                .startObjectField("postal")
                .put("code", "55401")
                .end()
                .startObjectField("city")
                .put("geoname_id", 9876)
                .startObjectField("names")
                .put("en", "Minneapolis")
                .end()
                .end()
                .end()
                .finish();

        testRoundTrip(CityResponse.class, json);
    }

    @Test
    public void testCountrySerialization() throws IOException {
        String json = JSON.std
                .composeString()
                .startObject()
                .startObjectField("maxmind")
                .put("queries_remaining", 11)
                .end()
                .startObjectField("registered_country")
                .put("geoname_id", 2)
                .startObjectField("names")
                .put("en", "Canada")
                .end()
                .put("iso_code", "CA")
                .end()
                .startObjectField("traits")
                .put("is_anonymous_proxy", true)
                .put("ip_address", "1.2.3.4")
                .put("is_satellite_provider", true)
                // This is here just to simplify the testing. We expect the
                // difference
                .put("is_legitimate_proxy", false)
                .end()
                .startObjectField("country")
                .startObjectField("names")
                .put("en", "United States of America")
                .end()
                .put("geoname_id", 1)
                .put("iso_code", "US")
                .end()
                .startObjectField("continent")
                .startObjectField("names")
                .put("en", "North America")
                .end()
                .put("code", "NA")
                .put("geoname_id", 42)
                .end()
                .startObjectField("represented_country")
                .put("geoname_id", 3)
                .startObjectField("names")
                .put("en", "United Kingdom")
                .end()
                .put("type", "C<military>")
                .put("iso_code", "GB")
                .end()
                .end()
                .finish();

        testRoundTrip(CountryResponse.class, json);
    }

    @Test
    public void testAnonymousIPSerialization() throws Exception {
        String json = JSON.std
                .composeString()
                .startObject()
                .put("is_anonymous", true)
                .put("is_anonymous_vpn", true)
                .put("is_hosting_provider", true)
                .put("is_public_proxy", true)
                .put("is_tor_exit_node", true)
                .put("ip_address", "1.1.1.1")
                .end()
                .finish();

        testRoundTrip(AnonymousIpResponse.class, json);
    }

    @Test
    public void testConnectionTypeSerialization() throws Exception {
        String json = JSON.std
                .composeString()
                .startObject()
                .put("connection_type", "Dialup")
                .put("ip_address", "1.1.1.1")
                .end()
                .finish();

        testRoundTrip(ConnectionTypeResponse.class, json);
    }

    @Test
    public void testDomainSerialization() throws Exception {
        String json = JSON.std
                .composeString()
                .startObject()
                .put("domain", "gmail.com")
                .put("ip_address", "1.1.1.1")
                .end()
                .finish();

        testRoundTrip(DomainResponse.class, json);
    }


    @Test
    public void testIspSerialization() throws Exception {
        String json = JSON.std
                .composeString()
                .startObject()
                .put("autonomous_system_number", 2121)
                .put("autonomous_system_organization", "Google, Inc.")
                .put("isp", "ISP, Inc.")
                .put("organization", "Google, Inc.")
                .put("ip_address", "1.1.1.1")
                .end()
                .finish();

        testRoundTrip(IspResponse.class, json);
    }

    protected <T extends AbstractResponse> void testRoundTrip
            (Class<T> cls, String json)
            throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS, false);
        InjectableValues inject = new InjectableValues.Std().addValue(
                "locales", Collections.singletonList("en"));
        T response = mapper.reader(cls).with(inject).readValue(json);

        JsonNode expectedNode = mapper.readValue(json, JsonNode.class);
        JsonNode actualNode = mapper.readValue(response.toJson(), JsonNode.class);

        assertEquals(expectedNode, actualNode);
    }

}
