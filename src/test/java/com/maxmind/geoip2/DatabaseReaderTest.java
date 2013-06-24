package com.maxmind.geoip2;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.maxmind.geoip2.model.City;
import com.maxmind.geoip2.model.Omni;

@SuppressWarnings("static-method")
public class DatabaseReaderTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testDefaultLanguage() throws IOException,
            AddressNotFoundException {
        DatabaseReader reader = new DatabaseReader(new File(
                "test-data/GeoIP2-City.mmdb"));
        City city = reader.get(InetAddress.getByName("81.2.69.160"));
        assertEquals("London", city.getCity().getName());
        reader.close();
    }

    @Test
    public void testLanguageList() throws IOException, AddressNotFoundException {
        DatabaseReader reader = new DatabaseReader(new File(
                "test-data/GeoIP2-City.mmdb"), Arrays.asList("xx", "ru",
                "pt-BR", "es", "en"));
        Omni city = reader.get(InetAddress.getByName("81.2.69.160"));
        assertEquals("Лондон", city.getCity().getName());
        reader.close();

    }

    @Test
    public void hasIpAddress() throws IOException, AddressNotFoundException {
        DatabaseReader reader = new DatabaseReader(new File(
                "test-data/GeoIP2-City.mmdb"));
        City city = reader.get(InetAddress.getByName("81.2.69.160"));
        assertEquals("81.2.69.160", city.getTraits().getIpAddress());
        reader.close();
    }

    @Test
    public void unknownAddress() throws IOException, AddressNotFoundException {
        this.exception.expect(AddressNotFoundException.class);
        this.exception
                .expectMessage(containsString("The address 10.10.10.10 is not in the database."));

        DatabaseReader reader = new DatabaseReader(new File(
                "test-data/GeoIP2-City.mmdb"));
        @SuppressWarnings("unused")
        City city = reader.get(InetAddress.getByName("10.10.10.10"));
        reader.close();
    }

}
