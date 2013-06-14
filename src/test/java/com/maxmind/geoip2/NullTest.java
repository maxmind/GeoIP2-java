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
import com.maxmind.geoip2.WebServiceClient;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.Omni;
import com.maxmind.geoip2.record.CityRecord;
import com.maxmind.geoip2.record.ContinentRecord;
import com.maxmind.geoip2.record.CountryRecord;
import com.maxmind.geoip2.record.LocationRecord;
import com.maxmind.geoip2.record.MaxMindRecord;
import com.maxmind.geoip2.record.AbstractNamedRecord;
import com.maxmind.geoip2.record.RepresentedCountryRecord;
import com.maxmind.geoip2.record.SubdivisionRecord;
import com.maxmind.geoip2.record.TraitsRecord;

public class NullTest {

    private final HttpTransport transport = new TestTransport();

    private final WebServiceClient client = new WebServiceClient.Builder(42, "abcdef123456")
            .transport(this.transport).build();

    @Test
    public void testDefaults() throws IOException, GeoIp2Exception {
        Omni omni = this.client.omni(InetAddress.getByName("1.2.3.13"));

        assertTrue(omni.toString().startsWith("Omni"));

        CityRecord city = omni.getCity();
        assertNotNull(city);
        assertNull(city.getConfidence());

        ContinentRecord continent = omni.getContinent();
        assertNotNull(continent);
        assertNull(continent.getCode());

        CountryRecord country = omni.getCountry();
        assertNotNull(country);

        LocationRecord location = omni.getLocation();
        assertNotNull(location);
        assertNull(location.getAccuracyRadius());
        assertNull(location.getLatitude());
        assertNull(location.getLongitude());
        assertNull(location.getMetroCode());
        assertNull(location.getTimeZone());
        assertEquals("Location []", location.toString());

        MaxMindRecord maxmind = omni.getMaxMind();
        assertNotNull(maxmind);
        assertNull(maxmind.getQueriesRemaining());

        assertNotNull(omni.getPostal());

        CountryRecord registeredCountry = omni.getRegisteredCountry();
        assertNotNull(registeredCountry);

        RepresentedCountryRecord representedCountry = omni.getRepresentedCountry();
        assertNotNull(representedCountry);
        assertNull(representedCountry.getType());

        List<SubdivisionRecord> subdivisions = omni.getSubdivisionsList();
        assertNotNull(subdivisions);
        assertTrue(subdivisions.isEmpty());

        SubdivisionRecord subdiv = omni.getMostSpecificSubdivision();
        assertNotNull(subdiv);
        assertNull(subdiv.getIsoCode());
        assertNull(subdiv.getConfidence());

        TraitsRecord traits = omni.getTraits();
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

        for (CountryRecord c : new CountryRecord[] { country, registeredCountry,
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
