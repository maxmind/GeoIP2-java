package com.maxmind.geoip2;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.maxmind.db.Reader;
import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.AnonymousIpResponse;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.ConnectionTypeResponse;
import com.maxmind.geoip2.model.ConnectionTypeResponse.ConnectionType;
import com.maxmind.geoip2.model.DomainResponse;
import com.maxmind.geoip2.model.EnterpriseResponse;
import com.maxmind.geoip2.model.IspResponse;

public class DatabaseReaderTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();
    private File geoipFile;
    private InputStream geoipStream;

    @Before
    public void setup() throws URISyntaxException, IOException {
        URL resource = DatabaseReaderTest.class.getResource("/maxmind-db/test-data/GeoIP2-City-Test.mmdb");
        geoipStream = resource.openStream();
        geoipFile = new File(resource.toURI());
    }

    @Test
    public void testDefaultLocaleFile() throws Exception {
        DatabaseProvider reader = new DatabaseReader.Builder(geoipFile).build();
        testDefaultLocale(reader);
    }

    @Test
    public void testDefaultLocaleURL() throws Exception {
        DatabaseProvider reader = new DatabaseReader.Builder(geoipStream).build();
        testDefaultLocale(reader);
        reader.close();

    }

    private void testDefaultLocale(DatabaseProvider reader) throws IOException, GeoIp2Exception {
        CityResponse city = reader.city(InetAddress.getByName("81.2.69.160"));
        assertEquals("London", city.getCity().getName());
        reader.close();
    }

    @Test
    public void testLocaleListFile() throws Exception {
        DatabaseProvider reader = new DatabaseReader.Builder(geoipFile).locales(Arrays.asList("xx", "ru", "pt-BR", "es", "en"))
                        .build();
        testLocaleList(reader);
    }

    @Test
    public void testLocaleListURL() throws Exception {
        DatabaseProvider reader = new DatabaseReader.Builder(geoipFile).locales(Arrays.asList("xx", "ru", "pt-BR", "es", "en"))
                        .build();
        testLocaleList(reader);
    }

    private void testLocaleList(DatabaseProvider reader) throws IOException, GeoIp2Exception {
        CityResponse city = reader.city(InetAddress.getByName("81.2.69.160"));
        assertEquals("Лондон", city.getCity().getName());
        reader.close();
    }

    @Test
    public void testMemoryModeFile() throws Exception {
        DatabaseProvider reader = new DatabaseReader.Builder(geoipFile).fileMode(Reader.FileMode.MEMORY).build();
        testMemoryMode(reader);
    }

    @Test
    public void testMemoryModeURL() throws Exception {
        DatabaseProvider reader = new DatabaseReader.Builder(geoipFile).fileMode(Reader.FileMode.MEMORY).build();
        testMemoryMode(reader);
    }

    private void testMemoryMode(DatabaseProvider reader) throws IOException, GeoIp2Exception {
        CityResponse city = reader.city(InetAddress.getByName("81.2.69.160"));
        assertEquals("London", city.getCity().getName());
        assertEquals(100, city.getLocation().getAccuracyRadius().longValue());
        reader.close();
    }

    @Test
    public void testReload() throws Exception {
        DatabaseProvider reader = new DatabaseReader.Builder(geoipFile).withReload(false).build();
        testDefaultLocale(reader);
    }

    @Test
    public void testReloadWithLazyInitialze() throws Exception {
        DatabaseProvider reader = new DatabaseReader.Builder(geoipFile).withReload(true).build();
        testDefaultLocale(reader);
    }

    @Test
    public void metadata() throws IOException {
        DatabaseProvider reader = new DatabaseReader.Builder(geoipFile).fileMode(Reader.FileMode.MEMORY).build();
        assertEquals("GeoIP2-City", reader.getMetadata().getDatabaseType());
        reader.close();
    }

    @Test
    public void hasIpAddressFile() throws Exception {
        DatabaseProvider reader = new DatabaseReader.Builder(geoipFile).build();
        hasIpAddress(reader);
    }

    @Test
    public void hasIpAddressURL() throws Exception {
        DatabaseProvider reader = new DatabaseReader.Builder(geoipFile).build();
        hasIpAddress(reader);
    }

    private void hasIpAddress(DatabaseProvider reader) throws IOException, GeoIp2Exception {
        CityResponse cio = reader.city(InetAddress.getByName("81.2.69.160"));
        assertEquals("81.2.69.160", cio.getTraits().getIpAddress());
        reader.close();
    }

    @Test
    public void unknownAddressFile() throws Exception {
        DatabaseProvider reader = new DatabaseReader.Builder(geoipFile).build();
        unknownAddress(reader);
    }

    @Test
    public void unknownAddressURL() throws Exception {
        DatabaseProvider reader = new DatabaseReader.Builder(geoipFile).build();
        unknownAddress(reader);
    }

    private void unknownAddress(DatabaseProvider reader) throws IOException, GeoIp2Exception {
        exception.expect(AddressNotFoundException.class);
        exception.expectMessage(containsString("The address 10.10.10.10 is not in the database."));
        try {
            reader.city(InetAddress.getByName("10.10.10.10"));
        } finally {
            reader.close();
        }
    }

    @Test
    public void testUnsupportedFileMode() throws IOException {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(containsString("Only FileMode.MEMORY"));
        new DatabaseReader.Builder(geoipStream).fileMode(Reader.FileMode.MEMORY_MAPPED).build();
    }

    @Test
    public void incorrectDatabaseMethod() throws Exception {
        exception.expect(UnsupportedOperationException.class);
        exception.expectMessage(containsString("GeoIP2-City database using the isp method"));
        DatabaseProvider db = new DatabaseReader.Builder(geoipFile).build();
        try {
            db.isp(InetAddress.getByName("1.1.1.1"));
        } finally {
            db.close();
        }
    }

    @Test
    public void testAnonymousIp() throws Exception {
        DatabaseProvider reader = new DatabaseReader.Builder(getFile("GeoIP2-Anonymous-IP-Test.mmdb")).build();
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
        DatabaseProvider reader = new DatabaseReader.Builder(getFile("GeoIP2-Connection-Type-Test.mmdb")).build();
        InetAddress ipAddress = InetAddress.getByName("1.0.1.0");

        ConnectionTypeResponse response = reader.connectionType(ipAddress);

        assertEquals(ConnectionType.CABLE_DSL, response.getConnectionType());
        assertEquals(ipAddress.getHostAddress(), response.getIpAddress());
        reader.close();
    }

    @Test
    public void testDomain() throws Exception {
        DatabaseProvider reader = new DatabaseReader.Builder(getFile("GeoIP2-Domain-Test.mmdb")).build();
        InetAddress ipAddress = InetAddress.getByName("1.2.0.0");
        DomainResponse response = reader.domain(ipAddress);
        assertEquals("maxmind.com", response.getDomain());
        assertEquals(ipAddress.getHostAddress(), response.getIpAddress());
        reader.close();
    }

    @Test
    public void testEnterprise() throws Exception {
        DatabaseProvider reader = new DatabaseReader.Builder(getFile("GeoIP2-Enterprise-Test.mmdb")).build();
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
        DatabaseProvider reader = new DatabaseReader.Builder(getFile("GeoIP2-ISP-Test.mmdb")).build();
        InetAddress ipAddress = InetAddress.getByName("1.128.0.0");
        IspResponse response = reader.isp(ipAddress);
        assertEquals(1221, response.getAutonomousSystemNumber().intValue());
        assertEquals("Telstra Pty Ltd", response.getAutonomousSystemOrganization());
        assertEquals("Telstra Internet", response.getIsp());
        assertEquals("Telstra Internet", response.getOrganization());

        assertEquals(ipAddress.getHostAddress(), response.getIpAddress());
        reader.close();
    }

    private File getFile(String filename) throws URISyntaxException {
        URL resource = DatabaseReaderTest.class.getResource("/maxmind-db/test-data/" + filename);
        return new File(resource.toURI());
    }
}
