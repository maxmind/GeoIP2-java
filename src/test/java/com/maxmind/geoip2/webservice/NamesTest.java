package com.maxmind.geoip2.webservice;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.LowLevelHttpResponse;
import com.google.api.client.testing.http.MockHttpTransport;
import com.google.api.client.testing.http.MockLowLevelHttpRequest;
import com.google.api.client.testing.http.MockLowLevelHttpResponse;
import com.maxmind.geoip2.exception.GeoIP2Exception;
import com.maxmind.geoip2.model.CityIspOrgResponse;

public class NamesTest {
    CityIspOrgResponse cio;

    @Before
    public void setUp() throws GeoIP2Exception {
        final String body = "{\"continent\":{" + "\"continent_code\":\"NA\","
                + "\"geoname_id\":42," + "\"names\":{"
                + "\"en\":\"North America\"," + "\"zh-CN\":\"北美洲\"" + "}"
                + "}," + "\"country\":{" + "\"geoname_id\":1,"
                + "\"iso_code\":\"US\"," + "\"confidence\":56," + "\"names\":{"
                + "\"en\":\"United States\","
                + "\"ru\":\"объединяет государства\"," + "\"zh-CN\":\"美国\""
                + "}" + "}," + "\"registered_country\":{" + "\"geoname_id\":2,"
                + "\"iso_code\":\"CA\"," + "\"names\":{\"en\":\"Canada\"}"
                + "}," + "\"traits\":{" + "\"ip_address\":\"1.2.3.4\"" + "}}";

        // Move this to shared code
        HttpTransport transport = new MockHttpTransport() {
            @Override
            public LowLevelHttpRequest buildRequest(String method, String url)
                    throws IOException {
                return new MockLowLevelHttpRequest() {
                    @Override
                    public LowLevelHttpResponse execute() throws IOException {
                        MockLowLevelHttpResponse response = new MockLowLevelHttpResponse();
                        response.addHeader("Content-Length",
                                String.valueOf(body.length()));
                        response.setStatusCode(200);
                        response.setContentType("application/vnd.maxmind.com-country"
                                + "+json; charset=UTF-8; version=1.0);");
                        response.setContent(body);
                        return response;
                    }
                };
            }
        };
        Client client = new Client(42, "012345689", "geoip.maxmind.com",
                transport);

        this.cio = client.cityIspOrg("1.1.1.1");
    }

    @Test
    public void testNames() {
        assertEquals(
                "country.getContinent().getName(\"zh-CN\") does not return 北美洲",
                "北美洲", this.cio.getContinent().getName("zh-CN"));
        assertEquals(
                "country.getCountry().getName(\"ru\") does not return объединяет государства",
                "объединяет государства", this.cio.getCountry().getName("ru"));
        assertEquals(
                "country.getCountry().getName(\"zh-CN\") does not return 美国",
                "美国", this.cio.getCountry().getName("zh-CN"));
    }
}
