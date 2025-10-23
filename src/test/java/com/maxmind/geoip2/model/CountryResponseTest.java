package com.maxmind.geoip2.model;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static com.maxmind.geoip2.json.File.readJsonFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.maxmind.geoip2.WebServiceClient;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

@WireMockTest
public class CountryResponseTest {
    @RegisterExtension
    static WireMockExtension wireMock = WireMockExtension.newInstance()
        .options(wireMockConfig().dynamicPort().dynamicHttpsPort())
        .build();

    private CountryResponse country;

    @BeforeEach
    public void createClient() throws IOException, GeoIp2Exception,
        URISyntaxException {
        wireMock.stubFor(get(urlEqualTo("/geoip/v2.1/country/1.1.1.1"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type",
                    "application/vnd.maxmind.com-country+json; charset=UTF-8; version=2.1")
                .withBody(readJsonFile("country0"))));

        WebServiceClient client = new WebServiceClient.Builder(6, "0123456789")
            .host("localhost")
            .port(wireMock.getPort())
            .disableHttps()
            .build();

        country = client.country(InetAddress.getByName("1.1.1.1"));
    }

    @Test
    public void testContinent() {
        assertEquals(
            "NA",
            this.country.continent().code(),
            "country.continent().code() does not return NA"
        );
        assertEquals(
            42,
            this.country.continent().geonameId(),
            "country.continent().geonameId() does not return 42"
        );
        assertEquals(
            "North America",
            this.country.continent().name(),
            "country.continent().name() does not return North America"
        );
    }

    @Test
    public void testCountry() {
        assertFalse(
            this.country.country().isInEuropeanUnion(),
            "country.country().isInEuropeanUnion() does not return false"
        );
        assertEquals(
            this.country.country().isoCode(),
            "US",
            "country.country().code() does not return US"
        );
        assertEquals(
            1,
            (long) this.country.country().geonameId(),
            "country.country().geonameId() does not return 1"
        );
        assertEquals(
            Integer.valueOf(56),
            this.country.country().confidence(),
            "country.country().confidence() does not return 56"
        );
        assertEquals(
            "United States",
            this.country.country().name(),
            "country.country().name(\"en\") does not return United States"
        );
    }

    @Test
    public void testRegisteredCountry() {
        assertFalse(
            this.country.registeredCountry().isInEuropeanUnion(),
            "country.registeredCountry().isInEuropeanUnion() does not return false"
        );
        assertEquals(
            "CA",
            this.country.registeredCountry().isoCode(),
            "country.registeredCountry().isoCode() does not return CA"
        );
        assertEquals(
            2,
            (long) this.country.registeredCountry().geonameId(),
            "country.registeredCountry().geonameId() does not return 2"
        );
        assertEquals(
            "Canada",
            this.country.registeredCountry().name(),
            "country.registeredCountry().name(\"en\") does not return United States"
        );
    }

    @Test
    public void testRepresentedCountry() {
        assertTrue(
            this.country.representedCountry().isInEuropeanUnion(),
            "country.representedCountry().isInEuropeanUnion() does not return true"
        );
        assertEquals(
            "GB",
            this.country.representedCountry().isoCode(),
            "country.representedCountry().code() does not return GB"
        );
        assertEquals(
            4,
            (long) this.country.representedCountry().geonameId(),
            "country.representedCountry().geonameId() does not return 4"
        );
        assertEquals(
            "United Kingdom",
            this.country.representedCountry().name(),
            "country.representedCountry().name(\"en\") does not return United Kingdom"
        );
        assertEquals(
            "military",
            this.country.representedCountry().type(),
            "country.representedCountry().type() does not return military"
        );
    }

    @Test
    public void testTraits() {

        assertEquals(
            "1.2.3.4",
            this.country.traits().ipAddress().getHostAddress(),
            "country.traits().getIpAddress does not return 1.2.3.4"
        );

    }
}
