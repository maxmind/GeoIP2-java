package com.maxmind.geoip2.webservice;

import static org.junit.Assert.assertEquals;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Test;

import com.google.api.client.http.HttpTransport;
import com.maxmind.geoip2.exception.GeoIP2Exception;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.webservice.Client;

public class CountryTest {
    private CountryResponse country;

    @Before
    public void setUp() throws GeoIP2Exception, UnknownHostException {
        HttpTransport transport = new TestTransport();
        Client client = new Client(42, "012345689", "geoip.maxmind.com",
                transport);

        this.country = client.country(InetAddress.getByName("1.1.1.3"));
    }

    @SuppressWarnings("boxing")
    @Test
    public void testContinent() {
        assertEquals("country.getContinent().getCode() does not return NA",
                "NA", this.country.getContinent().getCode());
        assertEquals(
                "country.getContinent().getGeoNameId() does not return 42", 42,
                (int) this.country.getContinent().getGeoNameId());
        assertEquals(
                "country.getContinent().getName(\"en\") does not return North America",
                "North America", this.country.getContinent().getName("en"));
    }

    @SuppressWarnings("boxing")
    @Test
    public void testCountry() {

        assertEquals("country.getCountry().getCode() does not return US", "US",
                this.country.getCountry().getIsoCode());
        assertEquals("country.getCountry().getGeoNameId() does not return 1",
                1, (int) this.country.getCountry().getGeoNameId());
        assertEquals("country.getCountry().getConfidence() does not return 56",
                new Integer(56), this.country.getCountry().getConfidence());
        assertEquals(
                "country.getCountry().getName(\"en\") does not return United States",
                "United States", this.country.getCountry().getName("en"));
    }

    @SuppressWarnings("boxing")
    @Test
    public void testRegisteredCountry() {
        assertEquals(
                "country.getRegisteredCountry().getCode() does not return CA",
                "CA", this.country.getRegisteredCountry().getIsoCode());
        assertEquals(
                "country.getRegisteredCountry().getGeoNameId() does not return 2",
                2, (int) this.country.getRegisteredCountry().getGeoNameId());
        assertEquals(
                "country.getRegisteredCountry().getName(\"en\") does not return United States",
                "Canada", this.country.getRegisteredCountry().getName("en"));
    }

    @SuppressWarnings("boxing")
    @Test
    public void testRepresentedCountry() {
        assertEquals(
                "country.getRepresentedCountry().getCode() does not return GA",
                "GB", this.country.getRepresentedCountry().getIsoCode());
        assertEquals(
                "country.getRepresentedCountry().getGeoNameId() does not return 4",
                4, (int) this.country.getRepresentedCountry().getGeoNameId());
        assertEquals(
                "country.getRepresentedCountry().getName(\"en\") does not return United Kingdom",
                "United Kingdom",
                this.country.getRepresentedCountry().getName("en"));
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
