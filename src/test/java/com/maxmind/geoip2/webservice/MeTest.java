package com.maxmind.geoip2.webservice;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.google.api.client.http.HttpTransport;
import com.maxmind.geoip2.exception.GeoIP2Exception;

public class MeTest {
    private Client client;

    @Before
    public void createClient() {
        HttpTransport transport = new TestTransport();

        this.client = new Client.Builder(42, "abcdef123456").transport(
                transport).build();

    }

    @Test
    public void omni() throws GeoIP2Exception {
        assertEquals(this.client.omni().getTraits().getIpAddress(),
                "24.24.24.24");
    }

    @Test
    public void cityIspOrg() throws GeoIP2Exception {
        assertEquals(this.client.cityIspOrg().getTraits().getIpAddress(),
                "24.24.24.24");
    }

    @Test
    public void city() throws GeoIP2Exception {
        assertEquals(this.client.city().getTraits().getIpAddress(),
                "24.24.24.24");
    }

    @Test
    public void country() throws GeoIP2Exception {
        assertEquals(this.client.country().getTraits().getIpAddress(),
                "24.24.24.24");
    }
}
