package com.maxmind.geoip2.webservice;

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
import com.maxmind.geoip2.exception.GeoIP2Exception;
import com.maxmind.geoip2.model.OmniLookup;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Continent;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import com.maxmind.geoip2.record.MaxMind;
import com.maxmind.geoip2.record.AbstractNamedRecord;
import com.maxmind.geoip2.record.RepresentedCountry;
import com.maxmind.geoip2.record.Subdivision;
import com.maxmind.geoip2.record.Traits;

public class NullTest {

    private final HttpTransport transport = new TestTransport();

    private final Client client = new Client.Builder(42, "abcdef123456")
            .transport(this.transport).build();

    @Test
    public void testDefaults() throws IOException, GeoIP2Exception {
        OmniLookup omni = this.client.omni(InetAddress.getByName("1.2.3.13"));

        assertTrue(omni.toString().startsWith("OmniLookup"));

        City city = omni.getCity();
        assertNotNull(city);
        assertNull(city.getConfidence());

        Continent continent = omni.getContinent();
        assertNotNull(continent);
        assertNull(continent.getCode());

        Country country = omni.getCountry();
        assertNotNull(country);

        Location location = omni.getLocation();
        assertNotNull(location);
        assertNull(location.getAccuracyRadius());
        assertNull(location.getLatitude());
        assertNull(location.getLongitude());
        assertNull(location.getMetroCode());
        assertNull(location.getTimeZone());
        assertEquals("Location []", location.toString());

        MaxMind maxmind = omni.getMaxMind();
        assertNotNull(maxmind);
        assertNull(maxmind.getQueriesRemaining());

        assertNotNull(omni.getPostal());

        Country registeredCountry = omni.getRegisteredCountry();
        assertNotNull(registeredCountry);

        RepresentedCountry representedCountry = omni.getRepresentedCountry();
        assertNotNull(representedCountry);
        assertNull(representedCountry.getType());

        List<Subdivision> subdivisions = omni.getSubdivisionsList();
        assertNotNull(subdivisions);
        assertTrue(subdivisions.isEmpty());

        Subdivision subdiv = omni.getMostSpecificSubdivision();
        assertNotNull(subdiv);
        assertNull(subdiv.getIsoCode());
        assertNull(subdiv.getConfidence());

        Traits traits = omni.getTraits();
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

        for (AbstractNamedRecord r : new AbstractNamedRecord[] { city, continent,
                country, registeredCountry, representedCountry, subdiv }) {
            assertNull(r.getGeoNameId());
            assertNull(r.getName());
            assertTrue(r.getNames().isEmpty());
            assertEquals("", r.toString());
        }
    }
}
