package com.maxmind.geoip2.webservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

import org.junit.Test;

import com.google.api.client.http.HttpTransport;
import com.maxmind.geoip2.exception.GeoIP2Exception;
import com.maxmind.geoip2.model.CityIspOrgLookup;

public class NamesTest {
    HttpTransport transport = new TestTransport();

    @Test
    public void testNames() throws GeoIP2Exception, UnknownHostException {
        Client client = new Client.Builder(42, "abcdef123456")
                .transport(this.transport)
                .languages(Arrays.asList("zh-CN", "ru")).build();

        CityIspOrgLookup cio = client.cityIspOrg(InetAddress
                .getByName("1.1.1.2"));
        assertEquals("country.getContinent().getName() does not return 北美洲",
                "北美洲", cio.getContinent().getName());
        assertEquals("country.getCountry().getName() does not return 美国", "美国",
                cio.getCountry().getName());
        assertEquals("toString() returns getName()",
                cio.getCountry().getName(), cio.getCountry().getName());
    }

    @Test
    public void russianFallback() throws GeoIP2Exception, UnknownHostException {
        Client client = new Client.Builder(42, "abcdef123456")
                .transport(this.transport).languages(Arrays.asList("as", "ru"))
                .build();

        CityIspOrgLookup cio = client.cityIspOrg(InetAddress
                .getByName("1.1.1.2"));
        assertEquals(
                "country.getCountry().getName() does not return объединяет государства",
                "объединяет государства", cio.getCountry().getName());

    }

    @Test
    public void testFallback() throws GeoIP2Exception, UnknownHostException {
        Client client = new Client.Builder(42, "abcdef123456")
                .transport(this.transport)
                .languages(Arrays.asList("pt", "en", "zh-CN")).build();
        CityIspOrgLookup cio = client.cityIspOrg(InetAddress
                .getByName("1.1.1.2"));
        assertEquals("en is returned when pt is missing", cio.getContinent()
                .getName(), "North America");

    }

    @Test
    public void noFallback() throws GeoIP2Exception, UnknownHostException {
        Client client = new Client.Builder(42, "abcdef123456")
                .transport(this.transport)
                .languages(Arrays.asList("pt", "es", "af")).build();
        CityIspOrgLookup cio = client.cityIspOrg(InetAddress
                .getByName("1.1.1.2"));

        assertNull("null is returned when language is not available", cio
                .getContinent().getName());
    }

    @Test
    public void noLanguage() throws GeoIP2Exception, UnknownHostException {
        Client client = new Client.Builder(42, "abcdef123456").transport(
                this.transport).build();
        CityIspOrgLookup cio = client.cityIspOrg(InetAddress
                .getByName("1.1.1.2"));
        assertEquals("en is returned when no languages are specified", cio
                .getContinent().getName(), "North America");

    }

    @Test
    public void testMissing() throws GeoIP2Exception, UnknownHostException {
        Client client = new Client.Builder(42, "abcdef123456")
                .transport(this.transport).languages(Arrays.asList("en"))
                .build();

        CityIspOrgLookup cio = client.cityIspOrg(InetAddress
                .getByName("1.1.1.2"));
        assertNotNull(cio.getCity());
        assertNull("null is returned when names object is missing", cio
                .getCity().getName());
    }
}
