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
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.*;

public class InsightsResponseTest {
    @Rule
    public final WireMockRule wireMockRule = new WireMockRule(0);

    // This really should be moved out of a string
    private final static String insightsBody =
            "{" +
                    "   \"city\" : {" +
                    "      \"confidence\" : 76," +
                    "      \"geoname_id\" : 9876," +
                    "      \"names\" : {" +
                    "         \"en\" : \"Minneapolis\"" +
                    "      }" +
                    "   }," +
                    "   \"continent\" : {" +
                    "      \"code\" : \"NA\"," +
                    "      \"geoname_id\" : 42," +
                    "      \"names\" : {" +
                    "         \"en\" : \"North America\"" +
                    "      }" +
                    "   }," +
                    "   \"country\" : {" +
                    "      \"confidence\" : 99," +
                    "      \"geoname_id\" : 1," +
                    "      \"iso_code\" : \"US\"," +
                    "      \"names\" : {" +
                    "         \"en\" : \"United States of America\"" +
                    "      }" +
                    "   }," +
                    "   \"location\" : {" +
                    "      \"accuracy_radius\" : 1500," +
                    "      \"average_income\" : 24626," +
                    "      \"latitude\" : 44.98," +
                    "      \"longitude\" : 93.2636," +
                    "      \"metro_code\" : 765," +
                    "      \"population_density\" : 1341," +
                    "      \"time_zone\" : \"America/Chicago\"" +
                    "   }," +
                    "   \"maxmind\" : {" +
                    "      \"queries_remaining\" : 11" +
                    "   }," +
                    "   \"postal\" : {" +
                    "      \"code\" : \"55401\"," +
                    "      \"confidence\" : 33" +
                    "   }," +
                    "   \"registered_country\" : {" +
                    "      \"geoname_id\" : 2," +
                    "      \"iso_code\" : \"CA\"," +
                    "      \"names\" : {" +
                    "         \"en\" : \"Canada\"" +
                    "      }" +
                    "   }," +
                    "   \"represented_country\" : {" +
                    "      \"geoname_id\" : 3," +
                    "      \"iso_code\" : \"GB\"," +
                    "      \"names\" : {" +
                    "         \"en\" : \"United Kingdom\"" +
                    "      }," +
                    "      \"type\" : \"C<military>\"" +
                    "   }," +
                    "   \"subdivisions\" : [" +
                    "      {" +
                    "         \"confidence\" : 88," +
                    "         \"geoname_id\" : 574635," +
                    "         \"iso_code\" : \"MN\"," +
                    "         \"names\" : {" +
                    "            \"en\" : \"Minnesota\"" +
                    "         }" +
                    "      }," +
                    "      {" +
                    "         \"iso_code\" : \"TT\"" +
                    "      }" +
                    "   ]," +
                    "   \"traits\" : {" +
                    "      \"autonomous_system_number\" : 1234," +
                    "      \"autonomous_system_organization\" : \"AS Organization\"," +
                    "      \"domain\" : \"example.com\"," +
                    "      \"ip_address\" : \"1.2.3.4\"," +
                    "      \"isp\" : \"Comcast\"," +
                    "      \"is_anonymous_proxy\" : true," +
                    "      \"is_satellite_provider\" : true," +
                    "      \"organization\" : \"Blorg\"," +
                    "      \"user_type\" : \"college\"" +
                    "   }" +
                    "}";

    private InsightsResponse insights;

    @Before
    public void createClient() throws IOException, GeoIp2Exception {
        stubFor(get(urlEqualTo("/geoip/v2.1/insights/1.1.1.1"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/vnd.maxmind.com-insights+json; charset=UTF-8; version=2.1")
                        .withBody(insightsBody.getBytes("UTF-8"))));

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
