package com.maxmind.geoip2;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;

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
import com.maxmind.geoip2.model.CityIspOrgResponse;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.ConnectionTypeResponse;
import com.maxmind.geoip2.model.ConnectionTypeResponse.ConnectionType;
import com.maxmind.geoip2.model.DomainResponse;
import com.maxmind.geoip2.model.IspResponse;
import com.maxmind.geoip2.model.OmniResponse;

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
    public void testDefaultLocaleFile() throws IOException, GeoIp2Exception {
        DatabaseReader reader = new DatabaseReader.Builder(this.geoipFile)
                .build();
        this.testDefaultLocale(reader);
    }

    @Test
    public void testDefaultLocaleURL() throws IOException, GeoIp2Exception {
        DatabaseReader reader = new DatabaseReader.Builder(this.geoipStream)
                .build();
        this.testDefaultLocale(reader);
    }

    private void testDefaultLocale(DatabaseReader reader) throws IOException,
            GeoIp2Exception {
        CityResponse city = reader.city(InetAddress.getByName("81.2.69.160"));
        assertEquals("London", city.getCity().getName());
        reader.close();
    }

    @Test
    public void testLocaleListFile() throws IOException, GeoIp2Exception {
        DatabaseReader reader = new DatabaseReader.Builder(this.geoipFile)
                .locales(Arrays.asList("xx", "ru", "pt-BR", "es", "en"))
                .build();
        this.testLocaleList(reader);
    }

    @Test
    public void testLocaleListURL() throws IOException, GeoIp2Exception {
        DatabaseReader reader = new DatabaseReader.Builder(this.geoipFile)
                .locales(Arrays.asList("xx", "ru", "pt-BR", "es", "en"))
                .build();
        this.testLocaleList(reader);
    }

    private void testLocaleList(DatabaseReader reader) throws IOException,
            GeoIp2Exception {
        OmniResponse city = reader.omni(InetAddress.getByName("81.2.69.160"));
        assertEquals("Лондон", city.getCity().getName());
        reader.close();
    }

    @Test
    public void testMemoryModeFile() throws IOException, GeoIp2Exception {
        DatabaseReader reader = new DatabaseReader.Builder(this.geoipFile)
                .fileMode(Reader.FileMode.MEMORY).build();
        this.testMemoryMode(reader);
    }

    @Test
    public void testMemoryModeURL() throws IOException, GeoIp2Exception {
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
    public void hasIpAddressFile() throws IOException, GeoIp2Exception {
        DatabaseReader reader = new DatabaseReader.Builder(this.geoipFile)
                .build();
        this.hasIpAddress(reader);
    }

    @Test
    public void hasIpAddressURL() throws IOException, GeoIp2Exception {
        DatabaseReader reader = new DatabaseReader.Builder(this.geoipFile)
                .build();
        this.hasIpAddress(reader);
    }

    private void hasIpAddress(DatabaseReader reader) throws IOException,
            GeoIp2Exception {
        CityIspOrgResponse cio = reader.cityIspOrg(InetAddress
                .getByName("81.2.69.160"));
        assertEquals("81.2.69.160", cio.getTraits().getIpAddress());
        reader.close();
    }

    @Test
    public void unknownAddressFile() throws IOException, GeoIp2Exception {
        DatabaseReader reader = new DatabaseReader.Builder(this.geoipFile)
                .build();
        this.unknownAddress(reader);
    }

    @Test
    public void unknownAddressURL() throws IOException, GeoIp2Exception {
        DatabaseReader reader = new DatabaseReader.Builder(this.geoipFile)
                .build();
        this.unknownAddress(reader);
    }

    private void unknownAddress(DatabaseReader reader) throws IOException,
            GeoIp2Exception {
        this.exception.expect(AddressNotFoundException.class);
        this.exception
                .expectMessage(containsString("The address 10.10.10.10 is not in the database."));

        reader.city(InetAddress.getByName("10.10.10.10"));
        reader.close();
    }

    @Test
    public void testUnsupportedFileMode() throws IOException {
        this.exception.expect(IllegalArgumentException.class);
        this.exception.expectMessage(containsString("Only FileMode.MEMORY"));
        new DatabaseReader.Builder(this.geoipStream).fileMode(
                Reader.FileMode.MEMORY_MAPPED).build();
    }

    @Test
    public void testConnectionType() throws IOException, URISyntaxException,
            GeoIp2Exception {
        DatabaseReader reader = new DatabaseReader.Builder(
                this.getFile("GeoIP2-Connection-Type-Test.mmdb")).build();
        InetAddress ipAddress = InetAddress.getByName("1.0.1.0");

        ConnectionTypeResponse response = reader.connectionType(ipAddress);

        assertEquals(ConnectionType.CABLE_DSL, response.getConnectionType());
        assertEquals(ipAddress.getHostAddress(), response.getIpAddress());
        reader.close();
    }

    @Test
    public void testDomain() throws IOException, URISyntaxException,
            GeoIp2Exception {
        DatabaseReader reader = new DatabaseReader.Builder(
                this.getFile("GeoIP2-Domain-Test.mmdb")).build();
        InetAddress ipAddress = InetAddress.getByName("1.2.0.0");
        DomainResponse response = reader.domain(ipAddress);
        assertEquals("maxmind.com", response.getDomain());
        assertEquals(ipAddress.getHostAddress(), response.getIpAddress());
        reader.close();
    }

    @Test
    public void testIsp() throws IOException, URISyntaxException,
            GeoIp2Exception {
        DatabaseReader reader = new DatabaseReader.Builder(
                this.getFile("GeoIP2-ISP-Org-Test.mmdb")).build();
        InetAddress ipAddress = InetAddress.getByName("2001:1700::");
        IspResponse response = reader.isp(ipAddress);
        assertEquals(6730, response.getAutonomousSystemNumber().intValue());
        assertEquals("Sunrise Communications AG",
                response.getAutonomousSystemOrganization());

        // XXX - Add org/isp when available

        assertEquals(ipAddress.getHostAddress(), response.getIpAddress());
        reader.close();
    }

    File getFile(String filename) throws URISyntaxException {
        URL resource = DatabaseReaderTest.class
                .getResource("/maxmind-db/test-data/" + filename);
        return new File(resource.toURI());
    }
}
