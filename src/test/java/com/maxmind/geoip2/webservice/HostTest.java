package com.maxmind.geoip2.webservice;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.InetAddress;

import org.junit.Test;

import com.google.api.client.http.HttpTransport;
import com.maxmind.geoip2.exception.GeoIP2Exception;
import com.maxmind.geoip2.model.OmniLookup;

public class HostTest {

    @Test
    public void omni() throws IOException, GeoIP2Exception {
        HttpTransport transport = new TestTransport();
        Client client = new Client.Builder(42, "abcdef123456").host("blah.com")
                .transport(transport).build();

        OmniLookup omni = client.omni(InetAddress.getByName("128.101.101.101"));
        assertEquals(omni.getTraits().getIpAddress(), "128.101.101.101");
    }
}
