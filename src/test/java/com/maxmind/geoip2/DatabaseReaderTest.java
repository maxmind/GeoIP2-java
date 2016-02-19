package com.maxmind.geoip2;

import com.maxmind.db.Reader;
import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.*;
import com.maxmind.geoip2.model.ConnectionTypeResponse.ConnectionType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

public class DatabaseReaderTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();
    private File geoipFile;
    private InputStream geoipStream;

    @Before
    public void setup() throws URISyntaxException, IOException {
        URL resource = DatabaseReaderTest.class
                .getResource("/maxmind-db/test-data/GeoIP2-City-Test.mmdb");
        this.geoipStream = resource.openStream();
        this.geoipFile = new File(resource.toURI());
    }

    @Test
    public void testDefaultLocaleFile() throws Exception {
        DatabaseReader reader = new DatabaseReader.Builder(this.geoipFile)
                .build();
        this.testDefaultLocale(reader);
    }

    @Test
    public void testDefaultLocaleURL() throws Exception {
        DatabaseReader reader = new DatabaseReader.Builder(this.geoipStream)
                .build();
        this.testDefaultLocale(reader);
        reader.close();

    }

    private void testDefaultLocale(DatabaseReader reader) throws IOException,
            GeoIp2Exception {
        CityResponse city = reader.city(InetAddress.getByName("81.2.69.160"));
        assertEquals("London", city.getCity().getName());
        reader.close();
    }

    @Test
    public void testLocaleListFile() throws Exception {
        DatabaseReader reader = new DatabaseReader.Builder(this.geoipFile)
                .locales(Arrays.asList("xx", "ru", "pt-BR", "es", "en"))
                .build();
        this.testLocaleList(reader);
    }

    @Test
    public void testLocaleListURL() throws Exception {
        DatabaseReader reader = new DatabaseReader.Builder(this.geoipFile)
                .locales(Arrays.asList("xx", "ru", "pt-BR", "es", "en"))
                .build();
        this.testLocaleList(reader);
    }

    private void testLocaleList(DatabaseReader reader) throws IOException,
            GeoIp2Exception {
        CityResponse city = reader.city(InetAddress.getByName("81.2.69.160"));
        assertEquals("Лондон", city.getCity().getName());
        reader.close();
    }

    @Test
    public void testMemoryModeFile() throws Exception {
        DatabaseReader reader = new DatabaseReader.Builder(this.geoipFile)
                .fileMode(Reader.FileMode.MEMORY).build();
        this.testMemoryMode(reader);
    }

    @Test
    public void testMemoryModeURL() throws Exception {
        DatabaseReader reader = new DatabaseReader.Builder(this.geoipFile)
                .fileMode(Reader.FileMode.MEMORY).build();
        this.testMemoryMode(reader);
    }

    private void testMemoryMode(DatabaseReader reader) throws IOException,
            GeoIp2Exception {
        CityResponse city = reader.city(InetAddress.getByName("81.2.69.160"));
        assertEquals("London", city.getCity().getName());
        reader.close();
    }

    @Test
    public void metadata() throws IOException {
        DatabaseReader reader = new DatabaseReader.Builder(this.geoipFile)
                .fileMode(Reader.FileMode.MEMORY).build();
        assertEquals("GeoIP2-City", reader.getMetadata().getDatabaseType());
        reader.close();
    }

    @Test
    public void hasIpAddressFile() throws Exception {
        DatabaseReader reader = new DatabaseReader.Builder(this.geoipFile)
                .build();
        this.hasIpAddress(reader);
    }

    @Test
    public void hasIpAddressURL() throws Exception {
        DatabaseReader reader = new DatabaseReader.Builder(this.geoipFile)
                .build();
        this.hasIpAddress(reader);
    }

    private void hasIpAddress(DatabaseReader reader) throws IOException,
            GeoIp2Exception {
        CityResponse cio = reader.city(InetAddress.getByName("81.2.69.160"));
        assertEquals("81.2.69.160", cio.getTraits().getIpAddress());
        reader.close();
    }

    @Test
    public void unknownAddressFile() throws Exception {
        DatabaseReader reader = new DatabaseReader.Builder(this.geoipFile)
                .build();
        this.unknownAddress(reader);
    }

    @Test
    public void unknownAddressURL() throws Exception {
        DatabaseReader reader = new DatabaseReader.Builder(this.geoipFile)
                .build();
        this.unknownAddress(reader);
    }

    private void unknownAddress(DatabaseReader reader) throws IOException,
            GeoIp2Exception {
        this.exception.expect(AddressNotFoundException.class);
        this.exception
                .expectMessage(containsString("The address 10.10.10.10 is not in the database."));
        try {
            reader.city(InetAddress.getByName("10.10.10.10"));
        } finally {
            reader.close();
        }
    }

    @Test
    public void testUnsupportedFileMode() throws IOException {
        this.exception.expect(IllegalArgumentException.class);
        this.exception.expectMessage(containsString("Only FileMode.MEMORY"));
        new DatabaseReader.Builder(this.geoipStream).fileMode(
                Reader.FileMode.MEMORY_MAPPED).build();
    }

    @Test
    public void incorrectDatabaseMethod() throws Exception {
        this.exception.expect(UnsupportedOperationException.class);
        this.exception
                .expectMessage(containsString("GeoIP2-City database using the isp method"));
        DatabaseReader db = new DatabaseReader.Builder(this.geoipFile).build();
        try {
            db.isp(InetAddress.getByName("1.1.1.1"));
        } finally {
            db.close();
        }
    }


    @Test
    public void testAnonymousIp() throws Exception {
        DatabaseReader reader = new DatabaseReader.Builder(
                this.getFile("GeoIP2-Anonymous-IP-Test.mmdb")).build();
        InetAddress ipAddress = InetAddress.getByName("1.2.0.1");
        AnonymousIpResponse response = reader.anonymousIp(ipAddress);
        assertTrue(response.isAnonymous());
        assertTrue(response.isAnonymousVpn());
        assertFalse(response.isHostingProvider());
        assertFalse(response.isPublicProxy());
        assertFalse(response.isTorExitNode());
        assertEquals(ipAddress.getHostAddress(), response.getIpAddress());
        reader.close();
    }

    @Test
    public void testConnectionType() throws Exception {
        DatabaseReader reader = new DatabaseReader.Builder(
                this.getFile("GeoIP2-Connection-Type-Test.mmdb")).build();
        InetAddress ipAddress = InetAddress.getByName("1.0.1.0");

        ConnectionTypeResponse response = reader.connectionType(ipAddress);

        assertEquals(ConnectionType.CABLE_DSL, response.getConnectionType());
        assertEquals(ipAddress.getHostAddress(), response.getIpAddress());
        reader.close();
    }

    @Test
    public void testDomain() throws Exception {
        DatabaseReader reader = new DatabaseReader.Builder(
                this.getFile("GeoIP2-Domain-Test.mmdb")).build();
        InetAddress ipAddress = InetAddress.getByName("1.2.0.0");
        DomainResponse response = reader.domain(ipAddress);
        assertEquals("maxmind.com", response.getDomain());
        assertEquals(ipAddress.getHostAddress(), response.getIpAddress());
        reader.close();
    }

    @Test
    public void testEnterprise() throws Exception {
        DatabaseReader reader = new DatabaseReader.Builder(
                getFile("GeoIP2-Enterprise-Test.mmdb")).build();
        InetAddress ipAddress = InetAddress.getByName("74.209.24.0");

        EnterpriseResponse response = reader.enterprise(ipAddress);
        assertEquals(11, response.getCity().getConfidence().intValue());
        assertEquals(99, response.getCountry().getConfidence().intValue());
        assertEquals(6252001, response.getCountry().getGeoNameId().intValue());
        assertEquals(27, response.getLocation().getAccuracyRadius().intValue());
        assertEquals(ConnectionType.CABLE_DSL, response.getTraits().getConnectionType());
        assertTrue(response.getTraits().isLegitimateProxy());
        assertEquals(ipAddress.getHostAddress(), response.getTraits().getIpAddress());
        reader.close();
    }

    @Test
    public void testIsp() throws Exception {
        DatabaseReader reader = new DatabaseReader.Builder(
                this.getFile("GeoIP2-ISP-Test.mmdb")).build();
        InetAddress ipAddress = InetAddress.getByName("1.128.0.0");
        IspResponse response = reader.isp(ipAddress);
        assertEquals(1221, response.getAutonomousSystemNumber().intValue());
        assertEquals("Telstra Pty Ltd",
                response.getAutonomousSystemOrganization());
        assertEquals("Telstra Internet", response.getIsp());
        assertEquals("Telstra Internet", response.getOrganization());

        assertEquals(ipAddress.getHostAddress(), response.getIpAddress());
        reader.close();
    }

    private File getFile(String filename) throws URISyntaxException {
        URL resource = DatabaseReaderTest.class
                .getResource("/maxmind-db/test-data/" + filename);
        return new File(resource.toURI());
    }
}
