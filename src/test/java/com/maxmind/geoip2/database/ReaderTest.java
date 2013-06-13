package com.maxmind.geoip2.database;

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
import com.maxmind.geoip2.exception.GeoIP2Exception;
import com.maxmind.geoip2.model.CityLookup;
import com.maxmind.geoip2.model.OmniLookup;

@SuppressWarnings("static-method")
public class ReaderTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testDefaultLanguage() throws IOException,
            AddressNotFoundException {
        Reader reader = new Reader(new File("test-data/GeoIP2-City.mmdb"));
        CityLookup city = reader.get(InetAddress.getByName("81.2.69.160"));
        assertEquals("London", city.getCity().getName());
        reader.close();
    }

    @Test
    public void testLanguageList() throws IOException, AddressNotFoundException {
        Reader reader = new Reader(new File("test-data/GeoIP2-City.mmdb"),
                Arrays.asList("xx", "ru", "pt-BR", "es", "en"));
        OmniLookup city = reader.get(InetAddress.getByName("81.2.69.160"));
        assertEquals("Лондон", city.getCity().getName());
        System.out.println(city.getTraits().getIpAddress());
        reader.close();

    }

    @Test
    public void hasIpAddress() throws IOException, AddressNotFoundException {
        Reader reader = new Reader(new File("test-data/GeoIP2-City.mmdb"));
        CityLookup city = reader.get(InetAddress.getByName("81.2.69.160"));
        assertEquals("81.2.69.160", city.getTraits().getIpAddress());
        reader.close();
    }

    @Test
    public void unknownAddress() throws IOException, AddressNotFoundException {
        this.exception.expect(AddressNotFoundException.class);
        this.exception
                .expectMessage(containsString("The address 10.10.10.10 is not in the database."));

        Reader reader = new Reader(new File("test-data/GeoIP2-City.mmdb"));
        @SuppressWarnings("unused")
        CityLookup city = reader.get(InetAddress.getByName("10.10.10.10"));
        reader.close();
    }

}
