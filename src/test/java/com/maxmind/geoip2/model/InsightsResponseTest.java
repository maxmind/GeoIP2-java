package com.maxmind.geoip2.model;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static com.maxmind.geoip2.json.File.readJsonFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.maxmind.geoip2.WebServiceClient;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.ConnectionTypeResponse.ConnectionType;
import com.maxmind.geoip2.record.Location;
import com.maxmind.geoip2.record.MaxMind;
import com.maxmind.geoip2.record.Postal;
import com.maxmind.geoip2.record.Subdivision;
import com.maxmind.geoip2.record.Traits;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

@WireMockTest
public class InsightsResponseTest {
    @RegisterExtension
    static WireMockExtension wireMock = WireMockExtension.newInstance()
        .options(wireMockConfig().dynamicPort().dynamicHttpsPort())
        .build();

    private InsightsResponse insights;

    @BeforeEach
    public void createClient() throws IOException, GeoIp2Exception,
        URISyntaxException {
        wireMock.stubFor(get(urlEqualTo("/geoip/v2.1/insights/1.1.1.1"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type",
                    "application/vnd.maxmind.com-insights+json; charset=UTF-8; version=2.1")
                .withBody(readJsonFile("insights0"))));
        wireMock.stubFor(get(urlEqualTo("/geoip/v2.1/insights/1.1.1.2"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type",
                    "application/vnd.maxmind.com-insights+json; charset=UTF-8; version=2.1")
                .withBody(readJsonFile("insights1"))));

        WebServiceClient client = new WebServiceClient.Builder(6, "0123456789")
            .host("localhost")
            .port(wireMock.getPort())
            .disableHttps()
            .build();

        this.insights = client.insights(InetAddress.getByName("1.1.1.1"));
    }

    @Test
    public void testSubdivisionsList() {
        List<Subdivision> subdivisionsList = this.insights.getSubdivisions();
        assertNotNull(subdivisionsList, "city.getSubdivisionsList returns null");
        if (subdivisionsList.isEmpty()) {
            fail("subdivisionsList is empty");
        }
        Subdivision subdivision = subdivisionsList.get(0);
        assertEquals(
            Integer.valueOf(88),
            subdivision.getConfidence(),
            "subdivision.getConfidence() does not return 88"
        );
        assertEquals(
            574635,
            subdivision.getGeoNameId().intValue(),
            "subdivision.getGeoNameId() does not return 574635"
        );
        assertEquals(
            "MN",
            subdivision.getIsoCode(),
            "subdivision.getCode() does not return MN"
        );
    }

    @Test
    public void mostSpecificSubdivision() {
        assertEquals(
            "TT",
            this.insights.getMostSpecificSubdivision().getIsoCode(),
            "Most specific subdivision returns last subdivision"
        );
    }

    @Test
    public void leastSpecificSubdivision() {
        assertEquals(
            "MN",
            this.insights.getLeastSpecificSubdivision().getIsoCode(),
            "Most specific subdivision returns first subdivision"
        );
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testTraits() {
        Traits traits = this.insights.getTraits();

        assertNotNull(traits, "city.getTraits() returns null");
        assertEquals(
            Long.valueOf(1234),
            traits.getAutonomousSystemNumber(),
            "traits.getAutonomousSystemNumber() does not return 1234"
        );
        assertEquals(

            "AS Organization",
            traits.getAutonomousSystemOrganization(),
            "traits.getAutonomousSystemOrganization() does not return AS Organization"
        );
        assertEquals(

            ConnectionType.CABLE_DSL,
            traits.getConnectionType(),
            "traits.getConnectionType() does not return Cable/DSL"
        );
        assertEquals(
            "example.com",
            traits.getDomain(),
            "traits.getDomain() does not return example.com"
        );
        assertEquals(
            "1.2.3.4",
            traits.getIpAddress(),
            "traits.getIpAddress() does not return 1.2.3.4"
        );
        assertTrue(traits.isAnonymous(), "traits.isAnonymous() returns true");
        assertTrue(traits.isAnonymousProxy(), "traits.isAnonymousProxy() returns true");
        assertTrue(traits.isAnonymousVpn(), "traits.isAnonymousVpn() returns true");
        assertTrue(traits.isHostingProvider(), "traits.isHostingProvider() returns true");
        assertTrue(traits.isPublicProxy(), "traits.isPublicProxy() returns true");
        assertTrue(traits.isResidentialProxy(), "traits.isResidentialProxy() returns true");
        assertTrue(traits.isSatelliteProvider(), "traits.isSatelliteProvider() returns true");
        assertTrue(traits.isTorExitNode(), "traits.isTorExitNode() returns true");
        assertEquals(
            "Comcast",
            traits.getIsp(),
            "traits.getIsp() does not return Comcast"
        );
        assertEquals(
            "Blorg",
            traits.getOrganization(),
            "traits.getOrganization() does not return Blorg"
        );
        assertEquals(
            "college",
            traits.getUserType(),
            "traits.getUserType() does not return userType"
        );
        assertEquals(
            Double.valueOf(1.3),
            traits.getStaticIpScore(),
            "traits.getStaticIpScore() does not return 1.3"
        );
        assertEquals(
            Integer.valueOf(2),
            traits.getUserCount(),
            "traits.getUserCount() does not return 2"
        );
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testLocation() {

        Location location = this.insights.getLocation();

        assertNotNull(location, "city.getLocation() returns null");

        assertEquals(
            Integer.valueOf(24626),
            location.getAverageIncome(),
            "location.getAverageIncome() does not return 24626"
        );

        assertEquals(
            Integer.valueOf(1500),
            location.getAccuracyRadius(),
            "location.getAccuracyRadius() does not return 1500"
        );

        double latitude = location.getLatitude();
        assertEquals(
            44.98,
            latitude,
            0.1,
            "location.getLatitude() does not return 44.98"
        );
        double longitude = location.getLongitude();
        assertEquals(
            93.2636,
            longitude,
            0.1,
            "location.getLongitude() does not return 93.2636"
        );
        assertEquals(
            Integer.valueOf(765),
            location.getMetroCode(),
            "location.getMetroCode() does not return 765"
        );
        assertEquals(
            Integer.valueOf(1341),
            location.getPopulationDensity(),
            "location.getPopulationDensity() does not return 1341"
        );
        assertEquals(
            "America/Chicago",
            location.getTimeZone(),
            "location.getTimeZone() does not return America/Chicago"
        );
    }

    @Test
    public void testMaxMind() {
        MaxMind maxmind = this.insights.getMaxMind();
        assertEquals(
            11, maxmind
                .getQueriesRemaining().intValue(),
            "Correct number of queries remaining"
        );
    }

    @Test
    public void testPostal() {

        Postal postal = this.insights.getPostal();
        assertEquals(
            "55401",
            postal.getCode(),
            "postal.getCode() does not return 55401"
        );
        assertEquals(
            Integer.valueOf(33),
            postal.getConfidence(),
            "postal.getConfidence() does not return 33"
        );
    }

    @Test
    public void testRepresentedCountry() {
        assertNotNull(
            this.insights.getRepresentedCountry(),
            "city.getRepresentedCountry() returns null"
        );

        assertEquals(
            "C<military>",
            this.insights.getRepresentedCountry().getType(),
            "city.getRepresentedCountry().getType() does not return C<military>"
        );
        assertTrue(
            this.insights.getRepresentedCountry().isInEuropeanUnion(),
            "city.getRepresentedCountry().isInEuropeanUnion() does not return true"
        );
    }

    @Test
    public void testIsInEuropeanUnion() throws IOException, GeoIp2Exception {
        // This uses an alternate fixture where we have the
        // is_in_european_union flag set in locations not set in the other
        // fixture.
        WebServiceClient client = new WebServiceClient.Builder(6, "0123456789")
            .host("localhost")
            .port(wireMock.getPort())
            .disableHttps()
            .build();

        InsightsResponse insights = client.insights(
            InetAddress.getByName("1.1.1.2"));

        assertTrue(
            insights.getCountry().isInEuropeanUnion(),
            "getCountry().isInEuropeanUnion() does not return true"
        );
        assertTrue(
            insights.getRegisteredCountry().isInEuropeanUnion(),
            "getRegisteredCountry().() isInEuropeanUnion = does not return true"
        );
    }
}
