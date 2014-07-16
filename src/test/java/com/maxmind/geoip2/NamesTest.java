package com.maxmind.geoip2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;

import org.junit.Test;

import com.google.api.client.http.HttpTransport;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;

public class NamesTest {
    HttpTransport transport = new TestTransport();

    @Test
    public void testNames() throws IOException, GeoIp2Exception {
        WebServiceClient client = new WebServiceClient.Builder(42,
                "abcdef123456").testTransport(this.transport)
                .locales(Arrays.asList("zh-CN", "ru")).build();

        CityResponse city = client.city(InetAddress
                .getByName("1.1.1.2"));
        assertEquals("country.getContinent().getName() does not return 北美洲",
                "北美洲", city.getContinent().getName());
        assertEquals("country.getCountry().getName() does not return 美国", "美国",
                city.getCountry().getName());
        assertEquals("toString() returns getName()",
                city.getCountry().getName(), city.getCountry().getName());
    }

    @Test
    public void russianFallback() throws IOException, GeoIp2Exception {
        WebServiceClient client = new WebServiceClient.Builder(42,
                "abcdef123456").testTransport(this.transport)
                .locales(Arrays.asList("as", "ru")).build();

        CityResponse city = client.city(InetAddress
                .getByName("1.1.1.2"));
        assertEquals(
                "country.getCountry().getName() does not return объединяет государства",
                "объединяет государства", city.getCountry().getName());

    }

    @Test
    public void testFallback() throws IOException, GeoIp2Exception {
        WebServiceClient client = new WebServiceClient.Builder(42,
                "abcdef123456").testTransport(this.transport)
                .locales(Arrays.asList("pt", "en", "zh-CN")).build();
        CityResponse city = client.city(InetAddress
                .getByName("1.1.1.2"));
        assertEquals("en is returned when pt is missing", city.getContinent()
                .getName(), "North America");

    }

    @Test
    public void noFallback() throws IOException, GeoIp2Exception {
        WebServiceClient client = new WebServiceClient.Builder(42,
                "abcdef123456").testTransport(this.transport)
                .locales(Arrays.asList("pt", "es", "af")).build();
        CityResponse city = client.city(InetAddress
                .getByName("1.1.1.2"));

        assertNull("null is returned when locale is not available", city
                .getContinent().getName());
    }

    @Test
    public void noLocale() throws IOException, GeoIp2Exception {
        WebServiceClient client = new WebServiceClient.Builder(42,
                "abcdef123456").testTransport(this.transport).build();
        CityResponse city = client.city(InetAddress
                .getByName("1.1.1.2"));
        assertEquals("en is returned when no locales are specified", city
                .getContinent().getName(), "North America");

    }

    @Test
    public void testMissing() throws IOException, GeoIp2Exception {
        WebServiceClient client = new WebServiceClient.Builder(42,
                "abcdef123456").testTransport(this.transport)
                .locales(Arrays.asList("en")).build();

        CityResponse city = client.city(InetAddress
                .getByName("1.1.1.2"));
        assertNotNull(city.getCity());
        assertNull("null is returned when names object is missing", city
                .getCity().getName());
    }
}
