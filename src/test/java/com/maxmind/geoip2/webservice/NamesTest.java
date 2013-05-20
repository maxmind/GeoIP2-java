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
        Client client = new Client(42, "012345689",
                Arrays.asList("zh-CN", "ru"), this.transport);

        CityIspOrgLookup cio = client.cityIspOrg(InetAddress
                .getByName("1.1.1.2"));
        assertEquals("country.getContinent().getName() does not return 北美洲",
                "北美洲", cio.getContinent().getName());
        assertEquals("country.getCountry().getName() does not return 美国", "美国",
                cio.getCountry().getName());
    }

    @Test
    public void russianFallback() throws GeoIP2Exception, UnknownHostException {
        Client client = new Client(42, "012345689", Arrays.asList("as", "ru"),
                this.transport);

        CityIspOrgLookup cio = client.cityIspOrg(InetAddress
                .getByName("1.1.1.2"));
        assertEquals(
                "country.getCountry().getName() does not return объединяет государства",
                "объединяет государства", cio.getCountry().getName());

    }

    @Test
    public void testFallback() throws GeoIP2Exception, UnknownHostException {
        Client client = new Client(42, "012345689", Arrays.asList("pt", "en",
                "zh-CN"), this.transport);
        CityIspOrgLookup cio = client.cityIspOrg(InetAddress
                .getByName("1.1.1.2"));
        assertEquals("en is returned when pt is missing", cio.getContinent()
                .getName(), "North America");

    }

    @Test
    public void noFallback() throws GeoIP2Exception, UnknownHostException {
        Client client = new Client(42, "012345689", Arrays.asList("pt", "es",
                "af"), this.transport);
        CityIspOrgLookup cio = client.cityIspOrg(InetAddress
                .getByName("1.1.1.2"));

        assertNull("null is returned when language is not available", cio
                .getContinent().getName());
    }

    @Test
    public void noLanguage() throws GeoIP2Exception, UnknownHostException {
        Client client = new Client(42, "012345689", this.transport);
        CityIspOrgLookup cio = client.cityIspOrg(InetAddress
                .getByName("1.1.1.2"));
        assertEquals("en is returned when no languages are specified", cio
                .getContinent().getName(), "North America");

    }

    @Test
    public void testMissing() throws GeoIP2Exception, UnknownHostException {
        Client client = new Client(42, "012345689", Arrays.asList("en"),
                this.transport);

        CityIspOrgLookup cio = client.cityIspOrg(InetAddress
                .getByName("1.1.1.2"));
        assertNotNull(cio.getCity());
        assertNull("null is returned when names object is missing", cio
                .getCity().getName());
    }
}
