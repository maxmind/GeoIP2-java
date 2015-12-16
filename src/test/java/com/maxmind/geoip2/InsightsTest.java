package com.maxmind.geoip2;

import com.google.api.client.http.HttpTransport;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.InsightsResponse;
import com.maxmind.geoip2.record.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

import static org.junit.Assert.*;

public class InsightsTest {
    private InsightsResponse insights;

    @Before
    public void createClient() throws IOException, GeoIp2Exception {
        HttpTransport transport = new TestTransport();

        WebServiceClient client = new WebServiceClient.Builder(42, "012345689")
                .testTransport(transport).build();

        this.insights = client.insights(InetAddress.getByName("1.1.1.1"));
    }

    @Test
    public void testSubdivisionsList() {
        List<Subdivision> subdivisionsList = this.insights.getSubdivisions();
        assertNotNull("city.getSubdivisionsList returns null", subdivisionsList);
        if (subdivisionsList.isEmpty()) {
            fail("subdivisionsList is empty");
        }
        Subdivision subdivision = subdivisionsList.get(0);
        assertEquals("subdivision.getConfidence() does not return 88",
                new Integer(88), subdivision.getConfidence());
        assertEquals("subdivision.getGeoNameId() does not return 574635",
                574635, subdivision.getGeoNameId().intValue());
        assertEquals("subdivision.getCode() does not return MN", "MN",
                subdivision.getIsoCode());
    }

    @Test
    public void mostSpecificSubdivision() {
        assertEquals("Most specific subdivision returns last subdivision",
                "TT", this.insights.getMostSpecificSubdivision().getIsoCode());
    }

    @Test
    public void leastSpecificSubdivision() {
        assertEquals("Most specific subdivision returns first subdivision",
                "MN", this.insights.getLeastSpecificSubdivision().getIsoCode());
    }

    @SuppressWarnings("boxing")
    @Test
    public void testTraits() {
        Traits traits = this.insights.getTraits();

        assertNotNull("city.getTraits() returns null", traits);
        assertEquals("traits.getAutonomousSystemNumber() does not return 1234",
                new Integer(1234), traits.getAutonomousSystemNumber());
        assertEquals(
                "traits.getAutonomousSystemOrganization() does not return AS Organization",
                "AS Organization", traits.getAutonomousSystemOrganization());
        assertEquals(
                "traits.getAutonomousSystemOrganization() does not return example.com",
                "example.com", traits.getDomain());
        assertEquals("traits.getIpAddress() does not return 1.2.3.4",
                "1.2.3.4", traits.getIpAddress());
        assertTrue("traits.isAnonymousProxy() returns true", traits.isAnonymousProxy());
        assertTrue("traits.isSatelliteProvider() returns true", traits.isSatelliteProvider());
        assertEquals("traits.getIsp() does not return Comcast", "Comcast",
                traits.getIsp());
        assertEquals("traits.getOrganization() does not return Blorg", "Blorg",
                traits.getOrganization());
        assertEquals("traits.getUserType() does not return userType",
                "college", traits.getUserType());
    }

    @Test
    public void testLocation() {

        Location location = this.insights.getLocation();

        assertNotNull("city.getLocation() returns null", location);

        assertEquals("location.getAverageIncome() does not return 24626,",
                new Integer(24626), location.getAverageIncome());

        assertEquals("location.getAccuracyRadius() does not return 1500",
                new Integer(1500), location.getAccuracyRadius());

        double latitude = location.getLatitude();
        assertEquals("location.getLatitude() does not return 44.98", 44.98,
                latitude, 0.1);
        double longitude = location.getLongitude();
        assertEquals("location.getLongitude() does not return 93.2636",
                93.2636, longitude, 0.1);
        assertEquals("location.getMetroCode() does not return 765",
                new Integer(765), location.getMetroCode());
        assertEquals("location.getPopulationDensity() does not return 1341,",
                new Integer(1341), location.getPopulationDensity());
        assertEquals("location.getTimeZone() does not return America/Chicago",
                "America/Chicago", location.getTimeZone());
    }

    @Test
    public void testMaxMind() {
        MaxMind maxmind = this.insights.getMaxMind();
        assertEquals("Correct number of queries remaining", 11, maxmind
                .getQueriesRemaining().intValue());
    }

    @Test
    public void testPostal() {

        Postal postal = this.insights.getPostal();
        assertEquals("postal.getCode() does not return 55401", "55401",
                postal.getCode());
        assertEquals("postal.getConfidence() does not return 33", new Integer(
                33), postal.getConfidence());

    }

    @Test
    public void testRepresentedCountry() {
        assertNotNull("city.getRepresentedCountry() returns null",
                this.insights.getRepresentedCountry());

        assertEquals(
                "city.getRepresentedCountry().getType() does not return C<military>",
                "C<military>", this.insights.getRepresentedCountry().getType());
    }
}
