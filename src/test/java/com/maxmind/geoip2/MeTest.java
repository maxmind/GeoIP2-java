package com.maxmind.geoip2;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.google.api.client.http.HttpTransport;
import com.maxmind.geoip2.exception.GeoIp2Exception;

public class MeTest {
    private WebServiceClient client;

    @Before
    public void createClient() {
        HttpTransport transport = new TestTransport();

        this.client = new WebServiceClient.Builder(42, "abcdef123456")
                .testTransport(transport).build();

    }

    @Test
    public void insights() throws IOException, GeoIp2Exception {
        assertEquals(this.client.insights().getTraits().getIpAddress(),
                "24.24.24.24");
    }

    @Test
    public void city() throws IOException, GeoIp2Exception {
        assertEquals(this.client.city().getTraits().getIpAddress(),
                "24.24.24.24");
    }

    @Test
    public void country() throws IOException, GeoIp2Exception {
        assertEquals(this.client.country().getTraits().getIpAddress(),
                "24.24.24.24");
    }
}
