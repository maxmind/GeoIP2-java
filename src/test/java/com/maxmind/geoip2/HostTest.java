package com.maxmind.geoip2;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.InetAddress;

import org.junit.Test;

import com.google.api.client.http.HttpTransport;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.InsightsResponse;

public class HostTest {

    @Test
    public void insights() throws Exception {
        HttpTransport transport = new TestTransport();
        WebServiceClient client = new WebServiceClient.Builder(42,
                "abcdef123456").host("blah.com").testTransport(transport)
                .build();

        InsightsResponse insights = client.insights(InetAddress
                .getByName("128.101.101.101"));
        assertEquals("128.101.101.101", insights.getTraits().getIpAddress());
    }
}
