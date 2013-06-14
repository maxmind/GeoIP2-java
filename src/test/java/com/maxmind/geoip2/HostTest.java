package com.maxmind.geoip2;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.InetAddress;

import org.junit.Test;

import com.google.api.client.http.HttpTransport;
import com.maxmind.geoip2.WebServiceClient;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.Omni;

public class HostTest {

    @Test
    public void omni() throws IOException, GeoIp2Exception {
        HttpTransport transport = new TestTransport();
        WebServiceClient client = new WebServiceClient.Builder(42, "abcdef123456").host("blah.com")
                .transport(transport).build();

        Omni omni = client.omni(InetAddress.getByName("128.101.101.101"));
        assertEquals(omni.getTraits().getIpAddress(), "128.101.101.101");
    }
}
