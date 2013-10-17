package com.maxmind.geoip2;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityIspOrgResponse;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.OmniResponse;

public class DatabaseReaderTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();
    private File geoipFile;

    @Before
    public void setup() throws URISyntaxException {
        URI uri = DatabaseReaderTest.class.getResource(
                "/maxmind-db/test-data/GeoIP2-City-Test.mmdb").toURI();
        this.geoipFile = new File(uri);
    }

    @Test
    public void testDefaultLocale() throws IOException, GeoIp2Exception {
        DatabaseReader reader = new DatabaseReader(this.geoipFile);
        CityResponse city = reader.city(InetAddress.getByName("81.2.69.160"));
        assertEquals("London", city.getCity().getName());
        reader.close();
    }

    @Test
    public void testLocaleList() throws IOException, GeoIp2Exception {
        DatabaseReader reader = new DatabaseReader(this.geoipFile,
                Arrays.asList("xx", "ru", "pt-BR", "es", "en"));
        OmniResponse city = reader.omni(InetAddress.getByName("81.2.69.160"));
        assertEquals("Лондон", city.getCity().getName());
        reader.close();

    }

    @Test
    public void hasIpAddress() throws IOException, GeoIp2Exception {
        DatabaseReader reader = new DatabaseReader(this.geoipFile);
        CityIspOrgResponse cio = reader.cityIspOrg(InetAddress
                .getByName("81.2.69.160"));
        assertEquals("81.2.69.160", cio.getTraits().getIpAddress());
        reader.close();
    }

    @Test
    public void unknownAddress() throws IOException, GeoIp2Exception {
        this.exception.expect(AddressNotFoundException.class);
        this.exception
                .expectMessage(containsString("The address 10.10.10.10 is not in the database."));

        DatabaseReader reader = new DatabaseReader(this.geoipFile);
        reader.city(InetAddress.getByName("10.10.10.10"));
        reader.close();
    }

}
