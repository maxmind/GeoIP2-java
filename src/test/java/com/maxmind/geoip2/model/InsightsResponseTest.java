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
        List<Subdivision> subdivisionsList = this.insights.subdivisions();
        assertNotNull(subdivisionsList, "city.getSubdivisionsList returns null");
        if (subdivisionsList.isEmpty()) {
            fail("subdivisionsList is empty");
        }
        Subdivision subdivision = subdivisionsList.get(0);
        assertEquals(
            Integer.valueOf(88),
            subdivision.confidence(),
            "subdivision.confidence() does not return 88"
        );
        assertEquals(
            574635,
            subdivision.geonameId().intValue(),
            "subdivision.geonameId() does not return 574635"
        );
        assertEquals(
            "MN",
            subdivision.isoCode(),
            "subdivision.code() does not return MN"
        );
    }

    @Test
    public void mostSpecificSubdivision() {
        assertEquals(
            "TT",
            this.insights.mostSpecificSubdivision().isoCode(),
            "Most specific subdivision returns last subdivision"
        );
    }

    @Test
    public void leastSpecificSubdivision() {
        assertEquals(
            "MN",
            this.insights.leastSpecificSubdivision().isoCode(),
            "Most specific subdivision returns first subdivision"
        );
    }

    @Test
    public void testTraits() {
        Traits traits = this.insights.traits();

        assertNotNull(traits, "city.traits() returns null");
        assertEquals(
            Long.valueOf(1234),
            traits.autonomousSystemNumber(),
            "traits.autonomousSystemNumber() does not return 1234"
        );
        assertEquals(

            "AS Organization",
            traits.autonomousSystemOrganization(),
            "traits.autonomousSystemOrganization() does not return AS Organization"
        );
        assertEquals(

            ConnectionType.CABLE_DSL,
            traits.connectionType(),
            "traits.connectionType() does not return Cable/DSL"
        );
        assertEquals(
            "example.com",
            traits.domain(),
            "traits.domain() does not return example.com"
        );
        assertEquals(
            "1.2.3.4",
            traits.ipAddress(),
            "traits.ipAddress() does not return 1.2.3.4"
        );
        assertTrue(traits.isAnonymous(), "traits.isAnonymous() returns true");
        assertTrue(traits.isAnonymousVpn(), "traits.isAnonymousVpn() returns true");
        assertTrue(traits.isHostingProvider(), "traits.isHostingProvider() returns true");
        assertTrue(traits.isPublicProxy(), "traits.isPublicProxy() returns true");
        assertTrue(traits.isResidentialProxy(), "traits.isResidentialProxy() returns true");
        assertTrue(traits.isTorExitNode(), "traits.isTorExitNode() returns true");
        assertEquals(
            "Comcast",
            traits.isp(),
            "traits.isp() does not return Comcast"
        );
        assertEquals(
            "Blorg",
            traits.organization(),
            "traits.organization() does not return Blorg"
        );
        assertEquals(
            "college",
            traits.userType(),
            "traits.userType() does not return userType"
        );
        assertEquals(
            Double.valueOf(1.3),
            traits.staticIpScore(),
            "traits.staticIpScore() does not return 1.3"
        );
        assertEquals(
            Integer.valueOf(2),
            traits.userCount(),
            "traits.userCount() does not return 2"
        );
    }

    @Test
    public void testLocation() {

        Location location = this.insights.location();

        assertNotNull(location, "city.location() returns null");

        assertEquals(
            Integer.valueOf(24626),
            location.averageIncome(),
            "location.averageIncome() does not return 24626"
        );

        assertEquals(
            Integer.valueOf(1500),
            location.accuracyRadius(),
            "location.accuracyRadius() does not return 1500"
        );

        double latitude = location.latitude();
        assertEquals(
            44.98,
            latitude,
            0.1,
            "location.latitude() does not return 44.98"
        );
        double longitude = location.longitude();
        assertEquals(
            93.2636,
            longitude,
            0.1,
            "location.longitude() does not return 93.2636"
        );
        assertEquals(
            Integer.valueOf(1341),
            location.populationDensity(),
            "location.populationDensity() does not return 1341"
        );
        assertEquals(
            "America/Chicago",
            location.timeZone(),
            "location.timeZone() does not return America/Chicago"
        );
    }

    @Test
    public void testMaxMind() {
        MaxMind maxmind = this.insights.maxmind();
        assertEquals(
            11, maxmind
                .queriesRemaining().intValue(),
            "Correct number of queries remaining"
        );
    }

    @Test
    public void testPostal() {

        Postal postal = this.insights.postal();
        assertEquals(
            "55401",
            postal.code(),
            "postal.code() does not return 55401"
        );
        assertEquals(
            Integer.valueOf(33),
            postal.confidence(),
            "postal.confidence() does not return 33"
        );
    }

    @Test
    public void testRepresentedCountry() {
        assertNotNull(
            this.insights.representedCountry(),
            "city.representedCountry() returns null"
        );

        assertEquals(
            "C<military>",
            this.insights.representedCountry().type(),
            "city.representedCountry().type() does not return C<military>"
        );
        assertTrue(
            this.insights.representedCountry().isInEuropeanUnion(),
            "city.representedCountry().isInEuropeanUnion() does not return true"
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
            insights.country().isInEuropeanUnion(),
            "getCountry().isInEuropeanUnion() does not return true"
        );
        assertTrue(
            insights.registeredCountry().isInEuropeanUnion(),
            "getRegisteredCountry().() isInEuropeanUnion = does not return true"
        );
    }
}
