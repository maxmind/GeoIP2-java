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

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.maxmind.geoip2.json.File.readJsonFile;
import static org.junit.Assert.*;

public class CountryResponseTest {
    @Rule
    public final WireMockRule wireMockRule = new WireMockRule(0);

    private CountryResponse country;

    @Before
    public void createClient() throws IOException, GeoIp2Exception,
            URISyntaxException {
        stubFor(get(urlEqualTo("/geoip/v2.1/country/1.1.1.1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/vnd.maxmind.com-country+json; charset=UTF-8; version=2.1")
                        .withBody(readJsonFile("country0"))));

        WebServiceClient client = new WebServiceClient.Builder(6, "0123456789")
                .host("localhost")
                .port(this.wireMockRule.port())
                .disableHttps()
                .build();

        country = client.country(InetAddress.getByName("1.1.1.1"));
    }

    @Test
    public void testContinent() {
        assertEquals("country.getContinent().getCode() does not return NA",
                "NA", this.country.getContinent().getCode());
        assertEquals(
                "country.getContinent().getGeoNameId() does not return 42", 42,
                (long) this.country.getContinent().getGeoNameId());
        assertEquals(
                "country.getContinent().getName() does not return North America",
                "North America", this.country.getContinent().getName());
    }

    @Test
    public void testCountry() {
        assertFalse(
                "country.getCountry().isInEuropeanUnion() does not return false",
                this.country.getCountry().isInEuropeanUnion());
        assertEquals("country.getCountry().getCode() does not return US", "US",
                this.country.getCountry().getIsoCode());
        assertEquals("country.getCountry().getGeoNameId() does not return 1",
                1, (long) this.country.getCountry().getGeoNameId());
        assertEquals("country.getCountry().getConfidence() does not return 56",
                Integer.valueOf(56), this.country.getCountry().getConfidence());
        assertEquals(
                "country.getCountry().getName(\"en\") does not return United States",
                "United States", this.country.getCountry().getName());
    }

    @Test
    public void testRegisteredCountry() {
        assertFalse(
                "country.getRegisteredCountry().isInEuropeanUnion() does not return false",
                this.country.getRegisteredCountry().isInEuropeanUnion());
        assertEquals(
                "country.getRegisteredCountry().getIsoCode() does not return CA",
                "CA", this.country.getRegisteredCountry().getIsoCode());
        assertEquals(
                "country.getRegisteredCountry().getGeoNameId() does not return 2",
                2, (long) this.country.getRegisteredCountry().getGeoNameId());
        assertEquals(
                "country.getRegisteredCountry().getName(\"en\") does not return United States",
                "Canada", this.country.getRegisteredCountry().getName());
    }

    @Test
    public void testRepresentedCountry() {
        assertTrue(
                "country.getRepresentedCountry().isInEuropeanUnion() does not return true",
                this.country.getRepresentedCountry().isInEuropeanUnion());
        assertEquals(
                "country.getRepresentedCountry().getCode() does not return GB",
                "GB", this.country.getRepresentedCountry().getIsoCode());
        assertEquals(
                "country.getRepresentedCountry().getGeoNameId() does not return 4",
                4, (long) this.country.getRepresentedCountry().getGeoNameId());
        assertEquals(
                "country.getRepresentedCountry().getName(\"en\") does not return United Kingdom",
                "United Kingdom", this.country.getRepresentedCountry()
                        .getName());
        assertEquals(
                "country.getRepresentedCountry().getType() does not return military",
                "military", this.country.getRepresentedCountry().getType());
    }

    @Test
    public void testTraits() {

        assertEquals(
                "country.getTraits().getIpAddress does not return 1.2.3.4",
                "1.2.3.4", this.country.getTraits().getIpAddress());

    }
}
