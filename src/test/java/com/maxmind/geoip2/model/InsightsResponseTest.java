package com.maxmind.geoip2.model;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.maxmind.geoip2.WebServiceClient;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.record.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.maxmind.geoip2.json.File.readJsonFile;
import static org.junit.Assert.*;

public class InsightsResponseTest {
    @Rule
    public final WireMockRule wireMockRule = new WireMockRule(0);

    private InsightsResponse insights;

    @Before
    public void createClient() throws IOException, GeoIp2Exception,
        URISyntaxException {
        stubFor(get(urlEqualTo("/geoip/v2.1/insights/1.1.1.1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/vnd.maxmind.com-insights+json; charset=UTF-8; version=2.1")
                        .withBody(readJsonFile("insights0"))));
        stubFor(get(urlEqualTo("/geoip/v2.1/insights/1.1.1.2"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/vnd.maxmind.com-insights+json; charset=UTF-8; version=2.1")
                        .withBody(readJsonFile("insights1"))));

        WebServiceClient client = new WebServiceClient.Builder(6, "0123456789")
                .host("localhost")
                .port(this.wireMockRule.port())
                .disableHttps()
                .build();

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
        assertTrue("traits.isAnonymous() returns true", traits.isAnonymous());
        assertTrue("traits.isAnonymousProxy() returns true", traits.isAnonymousProxy());
        assertTrue("traits.isAnonymousVpn() returns true", traits.isAnonymousVpn());
        assertTrue("traits.isHostingProvider() returns true", traits.isHostingProvider());
        assertTrue("traits.isPublicProxy() returns true", traits.isPublicProxy());
        assertTrue("traits.isSatelliteProvider() returns true", traits.isSatelliteProvider());
        assertTrue("traits.isTorExitNode() returns true", traits.isTorExitNode());
        assertEquals("traits.getIsp() does not return Comcast", "Comcast",
                traits.getIsp());
        assertEquals("traits.getOrganization() does not return Blorg", "Blorg",
                traits.getOrganization());
        assertEquals("traits.getUserType() does not return userType",
                "college", traits.getUserType());
        assertEquals("traits.getStaticIpScore() does not return 1.3",
                Double.valueOf(1.3), traits.getStaticIpScore());
        assertEquals("traits.getUserCount() does not return 2",
                Integer.valueOf(2), traits.getUserCount());
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
        assertTrue(
                "city.getRepresentedCountry().isInEuropeanUnion() does not return true",
                this.insights.getRepresentedCountry().isInEuropeanUnion());
    }

    @Test
    public void testIsInEuropeanUnion() throws IOException, GeoIp2Exception {
        // This uses an alternate fixture where we have the
        // is_in_european_union flag set in locations not set in the other
        // fixture.
        WebServiceClient client = new WebServiceClient.Builder(6, "0123456789")
                .host("localhost")
                .port(this.wireMockRule.port())
                .disableHttps()
                .build();

        InsightsResponse insights = client.insights(
                InetAddress.getByName("1.1.1.2"));

        assertTrue("getCountry().isInEuropeanUnion() does not return true",
                insights.getCountry().isInEuropeanUnion());
        assertTrue(
                "getRegisteredCountry().() isInEuropeanUnion = does not return true",
                insights.getRegisteredCountry().isInEuropeanUnion());
    }
}
