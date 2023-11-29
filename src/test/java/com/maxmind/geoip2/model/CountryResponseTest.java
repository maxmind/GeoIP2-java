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
            this.country.getContinent().getCode(),
            "country.getContinent().getCode() does not return NA"
        );
        assertEquals(
            42,
            this.country.getContinent().getGeoNameId(),
            "country.getContinent().getGeoNameId() does not return 42"
        );
        assertEquals(
            "North America",
            this.country.getContinent().getName(),
            "country.getContinent().getName() does not return North America"
        );
    }

    @Test
    public void testCountry() {
        assertFalse(
            this.country.getCountry().isInEuropeanUnion(),
            "country.getCountry().isInEuropeanUnion() does not return false"
        );
        assertEquals(
            this.country.getCountry().getIsoCode(),
            "US",
            "country.getCountry().getCode() does not return US"
        );
        assertEquals(
            1,
            (long) this.country.getCountry().getGeoNameId(),
            "country.getCountry().getGeoNameId() does not return 1"
        );
        assertEquals(
            Integer.valueOf(56),
            this.country.getCountry().getConfidence(),
            "country.getCountry().getConfidence() does not return 56"
        );
        assertEquals(
            "United States",
            this.country.getCountry().getName(),
            "country.getCountry().getName(\"en\") does not return United States"
        );
    }

    @Test
    public void testRegisteredCountry() {
        assertFalse(
            this.country.getRegisteredCountry().isInEuropeanUnion(),
            "country.getRegisteredCountry().isInEuropeanUnion() does not return false"
        );
        assertEquals(
            "CA",
            this.country.getRegisteredCountry().getIsoCode(),
            "country.getRegisteredCountry().getIsoCode() does not return CA"
        );
        assertEquals(
            2,
            (long) this.country.getRegisteredCountry().getGeoNameId(),
            "country.getRegisteredCountry().getGeoNameId() does not return 2"
        );
        assertEquals(
            "Canada",
            this.country.getRegisteredCountry().getName(),
            "country.getRegisteredCountry().getName(\"en\") does not return United States"
        );
    }

    @Test
    public void testRepresentedCountry() {
        assertTrue(
            this.country.getRepresentedCountry().isInEuropeanUnion(),
            "country.getRepresentedCountry().isInEuropeanUnion() does not return true"
        );
        assertEquals(
            "GB",
            this.country.getRepresentedCountry().getIsoCode(),
            "country.getRepresentedCountry().getCode() does not return GB"
        );
        assertEquals(
            4,
            (long) this.country.getRepresentedCountry().getGeoNameId(),
            "country.getRepresentedCountry().getGeoNameId() does not return 4"
        );
        assertEquals(
            "United Kingdom",
            this.country.getRepresentedCountry().getName(),
            "country.getRepresentedCountry().getName(\"en\") does not return United Kingdom"
        );
        assertEquals(
            "military",
            this.country.getRepresentedCountry().getType(),
            "country.getRepresentedCountry().getType() does not return military"
        );
    }

    @Test
    public void testTraits() {

        assertEquals(
            "1.2.3.4",
            this.country.getTraits().getIpAddress(),
            "country.getTraits().getIpAddress does not return 1.2.3.4"
        );

    }
}
