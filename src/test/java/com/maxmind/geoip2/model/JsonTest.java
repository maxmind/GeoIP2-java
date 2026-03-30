package com.maxmind.geoip2.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.InjectableValues;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.jr.ob.JSON;
import java.io.IOException;
import java.util.Collections;
import org.junit.jupiter.api.Test;

public class JsonTest {

    @Test
    public void testInsightsSerialization() throws IOException {
        String json = JSON.std
            .composeString()
            .startObject()
            .startObjectProperty("maxmind")
            .put("queries_remaining", 11)
            .end()
            .startObjectProperty("registered_country")
            .put("geoname_id", 2)
            .startObjectProperty("names")
            .put("en", "Canada")
            .end()
            .put("is_in_european_union", false)
            .put("iso_code", "CA")
            .end()
            .startObjectProperty("traits")
            .put("autonomous_system_organization", "AS Organization")
            .put("autonomous_system_number", 1234)
            .put("domain", "example.com")
            .put("isp", "Comcast")
            .put("ip_address", "1.2.3.4")
            .put("is_anonymous", true)
            .put("is_anonymous_vpn", true)
            .put("is_anycast", true)
            .put("is_hosting_provider", true)
            .put("is_legitimate_proxy", true)
            .put("is_public_proxy", true)
            .put("is_residential_proxy", true)
            .put("is_tor_exit_node", true)
            .put("network", "1.2.3.0/24")
            .put("organization", "Blorg")
            .put("user_type", "college")
            .put("ip_risk_snapshot", 0.01)
            // This is here just to simplify the testing. We expect the
            // difference
            .put("is_legitimate_proxy", false)
            .end()
            .startObjectProperty("anonymizer")
            .put("confidence", 99)
            .put("is_anonymous", true)
            .put("is_anonymous_vpn", true)
            .put("is_hosting_provider", true)
            .put("is_public_proxy", true)
            .put("is_residential_proxy", true)
            .put("is_tor_exit_node", true)
            .put("network_last_seen", "2024-12-31")
            .put("provider_name", "NordVPN")
            .end()
            .startObjectProperty("country")
            .startObjectProperty("names")
            .put("en", "United States of America")
            .end()
            .put("geoname_id", 1)
            .put("is_in_european_union", false)
            .put("iso_code", "US")
            .put("confidence", 99)
            .end()
            .startObjectProperty("continent")
            .startObjectProperty("names")
            .put("en", "North America")
            .end()
            .put("code", "NA")
            .put("geoname_id", 42)
            .end()
            .startObjectProperty("location")
            .put("average_income", 24626)
            .put("population_density", 1341)
            .put("time_zone", "America/Chicago")
            .put("accuracy_radius", 1500)
            .put("latitude", 44.98)
            .put("longitude", 93.2636)
            .end()
            .startArrayProperty("subdivisions")
            .startObject()
            .put("confidence", 88)
            .put("iso_code", "MN")
            .put("geoname_id", 574635)
            .startObjectProperty("names")
            .put("en", "Minnesota")
            .end()
            .end()
            .startObject()
            .put("iso_code", "TT")
            .end()
            .end()
            .startObjectProperty("represented_country")
            .put("geoname_id", 3)
            .startObjectProperty("names")
            .put("en", "United Kingdom")
            .end()
            .put("type", "C<military>")
            .put("is_in_european_union", true)
            .put("iso_code", "GB")
            .end()
            .startObjectProperty("postal")
            .put("code", "55401")
            .put("confidence", 33)
            .end()
            .startObjectProperty("city")
            .put("confidence", 76)
            .put("geoname_id", 9876)
            .startObjectProperty("names")
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
            .startObjectProperty("maxmind")
            .put("queries_remaining", 11)
            .end()
            .startObjectProperty("registered_country")
            .put("geoname_id", 2)
            .startObjectProperty("names")
            .put("en", "Canada")
            .end()
            .put("is_in_european_union", false)
            .put("iso_code", "CA")
            .end()
            .startObjectProperty("traits")
            .put("autonomous_system_number", 1234)
            .put("isp", "Comcast")
            .put("ip_address", "1.2.3.4")
            .put("autonomous_system_organization", "AS Organization")
            .put("organization", "Blorg")
            .put("domain", "example.com")
            // These are here just to simplify the testing. We expect the
            // difference
            .put("is_anonymous", false)
            .put("is_anonymous_vpn", false)
            .put("is_anycast", true)
            .put("is_hosting_provider", false)
            .put("is_legitimate_proxy", false)
            .put("is_public_proxy", false)
            .put("is_residential_proxy", false)
            .put("is_tor_exit_node", false)
            .put("network", "1.2.3.0/24")
            .end()
            .startObjectProperty("country")
            .startObjectProperty("names")
            .put("en", "United States of America")
            .end()
            .put("geoname_id", 1)
            .put("is_in_european_union", false)
            .put("iso_code", "US")
            .end()
            .startObjectProperty("continent")
            .startObjectProperty("names")
            .put("en", "North America")
            .end()
            .put("code", "NA")
            .put("geoname_id", 42)
            .end()
            .startObjectProperty("location")
            .put("time_zone", "America/Chicago")
            .put("latitude", 44.98)
            .put("longitude", 93.2636)
            .end()
            .startArrayProperty("subdivisions")
            .startObject()
            .put("iso_code", "MN")
            .put("geoname_id", 574635)
            .startObjectProperty("names")
            .put("en", "Minnesota")
            .end()
            .end()
            .startObject()
            .put("iso_code", "TT")
            .end()
            .end()
            .startObjectProperty("represented_country")
            .put("geoname_id", 3)
            .startObjectProperty("names")
            .put("en", "United Kingdom")
            .end()
            .put("type", "C<military>")
            .put("is_in_european_union", true)
            .put("iso_code", "GB")
            .end()
            .startObjectProperty("postal")
            .put("code", "55401")
            .end()
            .startObjectProperty("city")
            .put("geoname_id", 9876)
            .startObjectProperty("names")
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
            .startObjectProperty("maxmind")
            .put("queries_remaining", 11)
            .end()
            .startObjectProperty("registered_country")
            .put("geoname_id", 2)
            .startObjectProperty("names")
            .put("en", "Canada")
            .end()
            .put("is_in_european_union", false)
            .put("iso_code", "CA")
            .end()
            .startObjectProperty("traits")
            .put("ip_address", "1.2.3.4")
            // These are here just to simplify the testing. We expect the
            // difference
            .put("is_anonymous", false)
            .put("is_anonymous_vpn", false)
            .put("is_anycast", true)
            .put("is_hosting_provider", false)
            .put("is_legitimate_proxy", false)
            .put("is_public_proxy", false)
            .put("is_residential_proxy", false)
            .put("is_tor_exit_node", false)
            .put("network", "1.2.3.0/24")
            .end()
            .startObjectProperty("country")
            .startObjectProperty("names")
            .put("en", "United States of America")
            .end()
            .put("geoname_id", 1)
            .put("is_in_european_union", false)
            .put("iso_code", "US")
            .end()
            .startObjectProperty("continent")
            .startObjectProperty("names")
            .put("en", "North America")
            .end()
            .put("code", "NA")
            .put("geoname_id", 42)
            .end()
            .startObjectProperty("represented_country")
            .put("geoname_id", 3)
            .startObjectProperty("names")
            .put("en", "United Kingdom")
            .end()
            .put("type", "C<military>")
            .put("is_in_european_union", true)
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
            .put("is_residential_proxy", false)
            .put("is_tor_exit_node", true)
            .put("ip_address", "1.1.1.1")
            .put("network", "1.1.1.0/24")
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
            .put("network", "1.1.1.0/24")
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
            .put("network", "1.1.1.0/24")
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
            .put("network", "1.1.1.0/24")
            .end()
            .finish();

        testRoundTrip(IspResponse.class, json);
    }

    protected <T extends com.maxmind.geoip2.JsonSerializable> void testRoundTrip
        (Class<T> cls, String json)
        throws IOException {
        JsonMapper mapper = JsonMapper.builder()
            .disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
            .addModule(new com.maxmind.geoip2.InetAddressModule())
            .build();
        InjectableValues inject = new InjectableValues.Std().addValue(
            "locales", Collections.singletonList("en"));
        T response = mapper.readerFor(cls).with(inject).readValue(json);

        JsonNode expectedNode = mapper.readValue(json, JsonNode.class);
        JsonNode actualNode = mapper.readValue(response.toJson(), JsonNode.class);

        assertEquals(expectedNode, actualNode);
    }
}
