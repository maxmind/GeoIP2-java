package com.maxmind.geoip2.model;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static com.maxmind.geoip2.json.File.readJsonFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.maxmind.geoip2.WebServiceClient;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

// In addition to testing the CityResponse, this code exercises the locale
// handling of the models
@WireMockTest
public class CityResponseTest {
    @RegisterExtension
    static WireMockExtension wireMock = WireMockExtension.newInstance()
        .options(wireMockConfig().dynamicPort().dynamicHttpsPort())
        .build();

    @BeforeEach
    public void createClient() throws IOException, GeoIp2Exception,
        URISyntaxException {
        wireMock.stubFor(get(urlEqualTo("/geoip/v2.1/city/1.1.1.2"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type",
                    "application/vnd.maxmind.com-city+json; charset=UTF-8; version=2.1")
                .withBody(readJsonFile("city0"))));
    }


    @Test
    public void testNames() throws Exception {
        WebServiceClient client = new WebServiceClient.Builder(6, "0123456789")
            .host("localhost")
            .port(wireMock.getPort())
            .disableHttps()
            .locales(Arrays.asList("zh-CN", "ru"))
            .build();

        CityResponse city = client.city(InetAddress.getByName("1.1.1.2"));
        assertEquals(
            "北美洲",
            city.getContinent().getName(),
            "country.getContinent().getName() does not return 北美洲"
        );
        assertEquals(
            "美国",
            city.getCountry().getName(),
            "country.getCountry().getName() does not return 美国"
        );
        assertEquals(
            city.getCountry()
                .getName(), city.getCountry().getName(),
            "toString() returns getName()"
        );
    }

    @Test
    public void russianFallback() throws Exception {
        WebServiceClient client = new WebServiceClient.Builder(42,
            "abcdef123456")
            .host("localhost")
            .port(wireMock.getPort())
            .disableHttps()
            .locales(Arrays.asList("as", "ru")).build();

        CityResponse city = client.city(InetAddress.getByName("1.1.1.2"));
        assertEquals(
            "объединяет государства",
            city.getCountry().getName(),
            "country.getCountry().getName() does not return объединяет государства"
        );

    }

    @Test
    public void testFallback() throws Exception {
        WebServiceClient client = new WebServiceClient.Builder(42,
            "abcdef123456")
            .host("localhost")
            .port(wireMock.getPort())
            .disableHttps()
            .locales(Arrays.asList("pt", "en", "zh-CN")).build();
        CityResponse city = client.city(InetAddress.getByName("1.1.1.2"));
        assertEquals(
            "North America",
            city.getContinent().getName(),
            "en is returned when pt is missing"
        );

    }

    @Test
    public void noFallback() throws Exception {
        WebServiceClient client = new WebServiceClient.Builder(42,
            "abcdef123456")
            .host("localhost")
            .port(wireMock.getPort())
            .disableHttps()
            .locales(Arrays.asList("pt", "es", "af")).build();
        CityResponse city = client.city(InetAddress.getByName("1.1.1.2"));

        assertNull(
            city.getContinent().getName(),
            "null is returned when locale is not available"
        );
    }

    @Test
    public void noLocale() throws Exception {
        WebServiceClient client = new WebServiceClient.Builder(42,
            "abcdef123456")
            .host("localhost")
            .port(wireMock.getPort())
            .disableHttps()
            .build();
        CityResponse city = client.city(InetAddress.getByName("1.1.1.2"));
        assertEquals(
            "North America",
            city.getContinent().getName(),
            "en is returned when no locales are specified"
        );

    }

    @Test
    public void testMissing() throws Exception {
        WebServiceClient client = new WebServiceClient.Builder(42,
            "abcdef123456")
            .host("localhost")
            .port(wireMock.getPort())
            .disableHttps()
            .locales(Collections.singletonList("en")).build();

        CityResponse city = client.city(InetAddress.getByName("1.1.1.2"));
        assertNotNull(city.getCity());
        assertNull(city.getCity().getName(), "null is returned when names object is missing");
    }
}
