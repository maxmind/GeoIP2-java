package com.maxmind.geoip2.model;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.maxmind.geoip2.WebServiceClient;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.maxmind.geoip2.json.File.readJsonFile;
import static org.junit.Assert.*;

// In addition to testing the CityResponse, this code exercises the locale
// handling of the models
public class CityResponseTest {
    @Rule
    public final WireMockRule wireMockRule = new WireMockRule(0);

    @Before
    public void createClient() throws IOException, GeoIp2Exception,
           URISyntaxException {
        stubFor(get(urlEqualTo("/geoip/v2.1/city/1.1.1.2"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/vnd.maxmind.com-city+json; charset=UTF-8; version=2.1")
                        .withBody(readJsonFile("city0"))));
    }


    @Test
    public void testNames() throws Exception {
        WebServiceClient client = new WebServiceClient.Builder(6, "0123456789")
                .host("localhost")
                .port(this.wireMockRule.port())
                .disableHttps()
                .locales(Arrays.asList("zh-CN", "ru"))
                .build();

        CityResponse city = client.city(InetAddress.getByName("1.1.1.2"));
        assertEquals("country.getContinent().getName() does not return 北美洲",
                "北美洲", city.getContinent().getName());
        assertEquals("country.getCountry().getName() does not return 美国", "美国",
                city.getCountry().getName());
        assertEquals("toString() returns getName()", city.getCountry()
                .getName(), city.getCountry().getName());
    }

    @Test
    public void russianFallback() throws Exception {
        WebServiceClient client = new WebServiceClient.Builder(42,
                "abcdef123456")
                .host("localhost")
                .port(this.wireMockRule.port())
                .disableHttps()
                .locales(Arrays.asList("as", "ru")).build();

        CityResponse city = client.city(InetAddress.getByName("1.1.1.2"));
        assertEquals(
                "country.getCountry().getName() does not return объединяет государства",
                "объединяет государства", city.getCountry().getName());

    }

    @Test
    public void testFallback() throws Exception {
        WebServiceClient client = new WebServiceClient.Builder(42,
                "abcdef123456")
                .host("localhost")
                .port(this.wireMockRule.port())
                .disableHttps()
                .locales(Arrays.asList("pt", "en", "zh-CN")).build();
        CityResponse city = client.city(InetAddress.getByName("1.1.1.2"));
        assertEquals("en is returned when pt is missing", "North America", city.getContinent()
                .getName());

    }

    @Test
    public void noFallback() throws Exception {
        WebServiceClient client = new WebServiceClient.Builder(42,
                "abcdef123456")
                .host("localhost")
                .port(this.wireMockRule.port())
                .disableHttps()
                .locales(Arrays.asList("pt", "es", "af")).build();
        CityResponse city = client.city(InetAddress.getByName("1.1.1.2"));

        assertNull("null is returned when locale is not available", city
                .getContinent().getName());
    }

    @Test
    public void noLocale() throws Exception {
        WebServiceClient client = new WebServiceClient.Builder(42,
                "abcdef123456")
                .host("localhost")
                .port(this.wireMockRule.port())
                .disableHttps()
                .build();
        CityResponse city = client.city(InetAddress.getByName("1.1.1.2"));
        assertEquals("en is returned when no locales are specified", "North America", city
                .getContinent().getName());

    }

    @Test
    public void testMissing() throws Exception {
        WebServiceClient client = new WebServiceClient.Builder(42,
                "abcdef123456")
                .host("localhost")
                .port(this.wireMockRule.port())
                .disableHttps()
                .locales(Collections.singletonList("en")).build();

        CityResponse city = client.city(InetAddress.getByName("1.1.1.2"));
        assertNotNull(city.getCity());
        assertNull("null is returned when names object is missing", city
                .getCity().getName());
    }
}
