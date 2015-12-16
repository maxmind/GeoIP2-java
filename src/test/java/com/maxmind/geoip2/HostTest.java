package com.maxmind.geoip2;

import com.google.api.client.http.HttpTransport;
import com.maxmind.geoip2.model.InsightsResponse;
import org.junit.Test;

import java.net.InetAddress;

import static org.junit.Assert.assertEquals;

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
