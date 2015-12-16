package com.maxmind.geoip2;

import com.google.api.client.http.HttpTransport;
import com.maxmind.geoip2.model.CityResponse;
import org.junit.Test;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

public class NamesTest {
    private final HttpTransport transport = new TestTransport();

    @Test
    public void testNames() throws Exception {
        WebServiceClient client = new WebServiceClient.Builder(42,
                "abcdef123456").testTransport(this.transport)
                .locales(Arrays.asList("zh-CN", "ru")).build();

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
                "abcdef123456").testTransport(this.transport)
                .locales(Arrays.asList("as", "ru")).build();

        CityResponse city = client.city(InetAddress.getByName("1.1.1.2"));
        assertEquals(
                "country.getCountry().getName() does not return объединяет государства",
                "объединяет государства", city.getCountry().getName());

    }

    @Test
    public void testFallback() throws Exception {
        WebServiceClient client = new WebServiceClient.Builder(42,
                "abcdef123456").testTransport(this.transport)
                .locales(Arrays.asList("pt", "en", "zh-CN")).build();
        CityResponse city = client.city(InetAddress.getByName("1.1.1.2"));
        assertEquals("en is returned when pt is missing", "North America", city.getContinent()
                .getName());

    }

    @Test
    public void noFallback() throws Exception {
        WebServiceClient client = new WebServiceClient.Builder(42,
                "abcdef123456").testTransport(this.transport)
                .locales(Arrays.asList("pt", "es", "af")).build();
        CityResponse city = client.city(InetAddress.getByName("1.1.1.2"));

        assertNull("null is returned when locale is not available", city
                .getContinent().getName());
    }

    @Test
    public void noLocale() throws Exception {
        WebServiceClient client = new WebServiceClient.Builder(42,
                "abcdef123456").testTransport(this.transport).build();
        CityResponse city = client.city(InetAddress.getByName("1.1.1.2"));
        assertEquals("en is returned when no locales are specified", "North America", city
                .getContinent().getName());

    }

    @Test
    public void testMissing() throws Exception {
        WebServiceClient client = new WebServiceClient.Builder(42,
                "abcdef123456").testTransport(this.transport)
                .locales(Collections.singletonList("en")).build();

        CityResponse city = client.city(InetAddress.getByName("1.1.1.2"));
        assertNotNull(city.getCity());
        assertNull("null is returned when names object is missing", city
                .getCity().getName());
    }
}
