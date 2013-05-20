package com.maxmind.geoip2.webservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Test;

import com.google.api.client.http.HttpTransport;
import com.maxmind.geoip2.exception.GeoIP2Exception;
import com.maxmind.geoip2.model.CityIspOrgResponse;
import com.maxmind.geoip2.webservice.Client;

public class NamesTest {
    private CityIspOrgResponse cio;

    @Before
    public void setUp() throws GeoIP2Exception, UnknownHostException {
        HttpTransport transport = new TestTransport();
        Client client = new Client(42, "012345689", "geoip.maxmind.com",
                transport);

        this.cio = client.cityIspOrg(InetAddress.getByName("1.1.1.2"));
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

    @Test
    public void testFallback() {
        assertEquals("en is returned when pt is missing", this.cio
                .getContinent().getName("pt", "en", "zh-CN"), "North America");
        assertNull("null is returned when language is not available", this.cio
                .getContinent().getName("pt", "ru", "de"));
        assertEquals("en is returned when no languages are specified", this.cio
                .getContinent().getName(), "North America");

    }

    @Test
    public void testMissing() {
        assertNotNull(this.cio.getCity());
        assertNull("null is returned when names object is missing", this.cio
                .getCity().getName("en"));
    }
}
