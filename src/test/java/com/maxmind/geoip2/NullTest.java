package com.maxmind.geoip2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

import org.junit.Test;

import com.google.api.client.http.HttpTransport;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.InsightsResponse;
import com.maxmind.geoip2.record.*;

public class NullTest {

    private final HttpTransport transport = new TestTransport();

    private final WebServiceClient client = new WebServiceClient.Builder(42,
            "abcdef123456").testTransport(this.transport).build();

    @Test
    public void testDefaults() throws IOException, GeoIp2Exception {
        InsightsResponse insights = this.client.insights(InetAddress.getByName("1.2.3.13"));

        assertTrue(insights.toString().startsWith("Insights"));

        City city = insights.getCity();
        assertNotNull(city);
        assertNull(city.getConfidence());

        Continent continent = insights.getContinent();
        assertNotNull(continent);
        assertNull(continent.getCode());

        Country country = insights.getCountry();
        assertNotNull(country);

        Location location = insights.getLocation();
        assertNotNull(location);
        assertNull(location.getAccuracyRadius());
        assertNull(location.getLatitude());
        assertNull(location.getLongitude());
        assertNull(location.getMetroCode());
        assertNull(location.getTimeZone());
        assertEquals("Location []", location.toString());

        MaxMind maxmind = insights.getMaxMind();
        assertNotNull(maxmind);
        assertNull(maxmind.getQueriesRemaining());

        assertNotNull(insights.getPostal());

        Country registeredCountry = insights.getRegisteredCountry();
        assertNotNull(registeredCountry);

        RepresentedCountry representedCountry = insights.getRepresentedCountry();
        assertNotNull(representedCountry);
        assertNull(representedCountry.getType());

        List<Subdivision> subdivisions = insights.getSubdivisions();
        assertNotNull(subdivisions);
        assertTrue(subdivisions.isEmpty());

        Subdivision subdiv = insights.getMostSpecificSubdivision();
        assertNotNull(subdiv);
        assertNull(subdiv.getIsoCode());
        assertNull(subdiv.getConfidence());

        Traits traits = insights.getTraits();
        assertNotNull(traits);
        assertNull(traits.getAutonomousSystemNumber());
        assertNull(traits.getAutonomousSystemOrganization());
        assertNull(traits.getDomain());
        assertNull(traits.getIpAddress());
        assertNull(traits.getIsp());
        assertNull(traits.getOrganization());
        assertNull(traits.getUserType());
        assertFalse(traits.isAnonymousProxy());
        assertFalse(traits.isSatelliteProvider());
        assertEquals(
                "Traits [anonymousProxy=false, satelliteProvider=false, ]",
                traits.toString());

        for (Country c : new Country[] { country, registeredCountry,
                representedCountry }) {
            assertNull(c.getConfidence());
            assertNull(c.getIsoCode());
        }

        for (AbstractNamedRecord r : new AbstractNamedRecord[] { city,
                continent, country, registeredCountry, representedCountry,
                subdiv }) {
            assertNull(r.getGeoNameId());
            assertNull(r.getName());
            assertTrue(r.getNames().isEmpty());
            assertEquals("", r.toString());
        }
    }
}
