package com.maxmind.geoip2;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.api.client.http.HttpTransport;
import com.maxmind.geoip2.WebServiceClient;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.City;

public class FallbackTest {
    private HttpTransport transport;
    private WebServiceClient client;

    @Before
    public void setUp() throws Exception {
        this.transport = new TestTransport();
        this.client = new WebServiceClient.Builder(42, "abcdef123456")
                .fallbackDatabase(new File("test-data/GeoIP2-City.mmdb"))
                .transport(this.transport).build();

    }

    @After
    public void tearDown() throws Exception {
        this.client.close();
    }

    @Test
    public void test() throws IOException, GeoIp2Exception {
        City city = this.client
                .city(InetAddress.getByName("81.2.69.160"));
        assertEquals("London", city.getCity().getName());
    }

}
