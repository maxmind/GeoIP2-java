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
    public void insights() throws Exception {
        assertEquals("24.24.24.24",
                this.client.insights().getTraits().getIpAddress());
    }

    @Test
    public void city() throws Exception {
        assertEquals("24.24.24.24",
                this.client.city().getTraits().getIpAddress());
    }

    @Test
    public void country() throws Exception {
        assertEquals("24.24.24.24",
                this.client.country().getTraits().getIpAddress());
    }
}
