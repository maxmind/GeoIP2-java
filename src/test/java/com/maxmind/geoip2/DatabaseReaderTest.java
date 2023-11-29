package com.maxmind.geoip2;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.maxmind.db.Reader;
import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.AnonymousIpResponse;
import com.maxmind.geoip2.model.AsnResponse;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.ConnectionTypeResponse;
import com.maxmind.geoip2.model.ConnectionTypeResponse.ConnectionType;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.model.DomainResponse;
import com.maxmind.geoip2.model.EnterpriseResponse;
import com.maxmind.geoip2.model.IspResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DatabaseReaderTest {
    private File geoipFile;
    private InputStream geoipStream;

    @BeforeEach
    public void setup() throws URISyntaxException, IOException {
        URL resource = DatabaseReaderTest.class
            .getResource("/maxmind-db/test-data/GeoIP2-City-Test.mmdb");
        this.geoipStream = resource.openStream();
        this.geoipFile = new File(resource.toURI());
    }

    @Test
    public void testDefaultLocaleFile() throws Exception {
        try (DatabaseReader reader = new DatabaseReader.Builder(this.geoipFile)
            .build()
        ) {
            this.testDefaultLocale(reader);
        }
    }

    @Test
    public void testDefaultLocaleURL() throws Exception {
        try (DatabaseReader reader = new DatabaseReader.Builder(this.geoipStream)
            .build()
        ) {
            this.testDefaultLocale(reader);
        }
    }

    private void testDefaultLocale(DatabaseReader reader) throws IOException,
        GeoIp2Exception {
        CityResponse city = reader.city(InetAddress.getByName("81.2.69.160"));
        assertEquals("London", city.getCity().getName());
    }

    @Test
    public void testIsInEuropeanUnion() throws Exception {
        try (DatabaseReader reader = new DatabaseReader.Builder(this.geoipFile)
            .build()
        ) {
            CityResponse city = reader.city(InetAddress.getByName("89.160.20.128"));
            assertTrue(city.getCountry().isInEuropeanUnion());
            assertTrue(city.getRegisteredCountry().isInEuropeanUnion());
        }
    }

    @Test
    public void testLocaleListFile() throws Exception {
        try (DatabaseReader reader = new DatabaseReader.Builder(this.geoipFile)
            .locales(Arrays.asList("xx", "ru", "pt-BR", "es", "en"))
            .build()
        ) {
            this.testLocaleList(reader);
        }
    }

    @Test
    public void testLocaleListURL() throws Exception {
        try (DatabaseReader reader = new DatabaseReader.Builder(this.geoipStream)
            .locales(Arrays.asList("xx", "ru", "pt-BR", "es", "en"))
            .build()
        ) {
            this.testLocaleList(reader);
        }
    }

    private void testLocaleList(DatabaseReader reader) throws IOException,
        GeoIp2Exception {
        CityResponse city = reader.city(InetAddress.getByName("81.2.69.160"));
        assertEquals("Лондон", city.getCity().getName());
    }

    @Test
    public void testMemoryModeFile() throws Exception {
        try (DatabaseReader reader = new DatabaseReader.Builder(this.geoipFile)
            .fileMode(Reader.FileMode.MEMORY).build()
        ) {
            this.testMemoryMode(reader);
        }
    }

    @Test
    public void testMemoryModeURL() throws Exception {
        try (DatabaseReader reader = new DatabaseReader.Builder(this.geoipStream)
            .fileMode(Reader.FileMode.MEMORY).build()
        ) {
            this.testMemoryMode(reader);
        }
    }

    private void testMemoryMode(DatabaseReader reader) throws IOException,
        GeoIp2Exception {
        CityResponse city = reader.city(InetAddress.getByName("81.2.69.160"));
        assertEquals("London", city.getCity().getName());
        assertEquals(100, city.getLocation().getAccuracyRadius().longValue());
    }

    @Test
    public void metadata() throws IOException {
        DatabaseReader reader = new DatabaseReader.Builder(this.geoipFile)
            .fileMode(Reader.FileMode.MEMORY).build();
        assertEquals("GeoIP2-City", reader.getMetadata().getDatabaseType());
    }

    @Test
    public void hasIpAddressFile() throws Exception {
        try (DatabaseReader reader = new DatabaseReader.Builder(this.geoipFile)
            .build()
        ) {
            this.hasIpInfo(reader);
        }
    }

    @Test
    public void hasIpAddressURL() throws Exception {
        try (DatabaseReader reader = new DatabaseReader.Builder(this.geoipStream)
            .build()
        ) {
            this.hasIpInfo(reader);
        }
    }

    private void hasIpInfo(DatabaseReader reader) throws IOException,
        GeoIp2Exception {
        CityResponse cio = reader.city(InetAddress.getByName("81.2.69.160"));
        assertEquals("81.2.69.160", cio.getTraits().getIpAddress());
        assertEquals("81.2.69.160/27", cio.getTraits().getNetwork().toString());
    }

    @Test
    public void unknownAddressFile() throws Exception {
        try (DatabaseReader reader = new DatabaseReader.Builder(this.geoipFile)
            .build()
        ) {
            this.unknownAddress(reader);
        }
    }

    @Test
    public void unknownAddressURL() throws Exception {
        try (DatabaseReader reader = new DatabaseReader.Builder(this.geoipStream)
            .build()
        ) {
            this.unknownAddress(reader);
        }
    }

    private void unknownAddress(DatabaseReader reader) throws IOException,
        GeoIp2Exception {
        assertFalse(reader.tryCity(InetAddress.getByName("10.10.10.10")).isPresent());

        Exception ex = assertThrows(AddressNotFoundException.class,
            () -> reader.city(InetAddress.getByName("10.10.10.10")));
        assertThat(ex.getMessage(),
            containsString("The address 10.10.10.10 is not in the database."));
    }

    @Test
    public void testUnsupportedFileMode() throws IOException {
        Exception ex = assertThrows(IllegalArgumentException.class,
            () -> new DatabaseReader.Builder(this.geoipStream).fileMode(
                Reader.FileMode.MEMORY_MAPPED).build()
        );
        assertThat(ex.getMessage(), containsString("Only FileMode.MEMORY"));
    }

    @Test
    public void incorrectDatabaseMethod() throws Exception {
        try (DatabaseReader db = new DatabaseReader.Builder(this.geoipFile).build()) {
            Exception ex = assertThrows(UnsupportedOperationException.class,
                () -> db.isp(InetAddress.getByName("1.1.1.1")));
            assertThat(ex.getMessage(),
                containsString("GeoIP2-City database using the isp method"));
        }
    }

    @Test
    public void testAnonymousIp() throws Exception {
        try (DatabaseReader reader = new DatabaseReader.Builder(
            this.getFile("GeoIP2-Anonymous-IP-Test.mmdb")).build()
        ) {
            InetAddress ipAddress = InetAddress.getByName("1.2.0.1");
            AnonymousIpResponse response = reader.anonymousIp(ipAddress);
            assertTrue(response.isAnonymous());
            assertTrue(response.isAnonymousVpn());
            assertFalse(response.isHostingProvider());
            assertFalse(response.isPublicProxy());
            assertFalse(response.isResidentialProxy());
            assertFalse(response.isTorExitNode());
            assertEquals(ipAddress.getHostAddress(), response.getIpAddress());
            assertEquals("1.2.0.0/16", response.getNetwork().toString());

            AnonymousIpResponse tryResponse = reader.tryAnonymousIp(ipAddress).get();
            assertEquals(response.toJson(), tryResponse.toJson());
        }
    }

    @Test
    public void testAnonymousIpIsResidentialProxy() throws Exception {
        try (DatabaseReader reader = new DatabaseReader.Builder(
            this.getFile("GeoIP2-Anonymous-IP-Test.mmdb")).build()
        ) {
            InetAddress ipAddress = InetAddress.getByName("81.2.69.1");
            AnonymousIpResponse response = reader.anonymousIp(ipAddress);
            assertTrue(response.isResidentialProxy());
        }
    }

    @Test
    public void testAsn() throws Exception {
        try (DatabaseReader reader = new DatabaseReader.Builder(
            this.getFile("GeoLite2-ASN-Test.mmdb")).build()
        ) {
            InetAddress ipAddress = InetAddress.getByName("1.128.0.0");
            AsnResponse response = reader.asn(ipAddress);
            assertEquals(1221, response.getAutonomousSystemNumber().intValue());
            assertEquals("Telstra Pty Ltd",
                response.getAutonomousSystemOrganization());
            assertEquals(ipAddress.getHostAddress(), response.getIpAddress());
            assertEquals("1.128.0.0/11", response.getNetwork().toString());

            AsnResponse tryResponse = reader.tryAsn(ipAddress).get();
            assertEquals(response.toJson(), tryResponse.toJson());
        }
    }

    @Test
    public void testCity() throws Exception {
        try (DatabaseReader reader = new DatabaseReader.Builder(
            getFile("GeoIP2-City-Test.mmdb")).build()
        ) {
            InetAddress ipAddress = InetAddress.getByName("81.2.69.192");

            CityResponse response = reader.city(ipAddress);
            assertEquals(2635167, response.getCountry().getGeoNameId().intValue());
            assertEquals(100, response.getLocation().getAccuracyRadius().intValue());
            assertFalse(response.getTraits().isLegitimateProxy());
            assertEquals(ipAddress.getHostAddress(), response.getTraits().getIpAddress());
            assertEquals("81.2.69.192/28", response.getTraits().getNetwork().toString());

            CityResponse tryResponse = reader.tryCity(ipAddress).get();
            assertEquals(response.toJson(), tryResponse.toJson());

            // Test that the methods can be called on DB without
            // an exception
            reader.country(ipAddress);
        }
    }

    @Test
    public void testConnectionType() throws Exception {
        try (DatabaseReader reader = new DatabaseReader.Builder(
            this.getFile("GeoIP2-Connection-Type-Test.mmdb")).build()
        ) {
            InetAddress ipAddress = InetAddress.getByName("1.0.1.0");

            ConnectionTypeResponse response = reader.connectionType(ipAddress);

            assertEquals(ConnectionType.CELLULAR, response.getConnectionType());
            assertEquals(ipAddress.getHostAddress(), response.getIpAddress());
            assertEquals("1.0.1.0/24", response.getNetwork().toString());

            ConnectionTypeResponse tryResponse = reader.tryConnectionType(ipAddress).get();
            assertEquals(response.toJson(), tryResponse.toJson());
        }
    }

    @Test
    public void testCountry() throws Exception {
        try (DatabaseReader reader = new DatabaseReader.Builder(
            getFile("GeoIP2-Country-Test.mmdb")).build()
        ) {
            InetAddress ipAddress = InetAddress.getByName("74.209.24.0");

            CountryResponse response = reader.country(ipAddress);
            assertEquals("NA", response.getContinent().getCode());
            assertEquals(6252001, response.getCountry().getGeoNameId().intValue());
            assertEquals(6252001, response.getRegisteredCountry().getGeoNameId().intValue());
            assertEquals(ipAddress.getHostAddress(), response.getTraits().getIpAddress());
            assertEquals("74.209.16.0/20", response.getTraits().getNetwork().toString());

            CountryResponse tryResponse = reader.tryCountry(ipAddress).get();
            assertEquals(response.toJson(), tryResponse.toJson());
        }
    }

    @Test
    public void testDomain() throws Exception {
        try (DatabaseReader reader = new DatabaseReader.Builder(
            this.getFile("GeoIP2-Domain-Test.mmdb")).build()
        ) {
            InetAddress ipAddress = InetAddress.getByName("1.2.0.0");
            DomainResponse response = reader.domain(ipAddress);
            assertEquals("maxmind.com", response.getDomain());
            assertEquals(ipAddress.getHostAddress(), response.getIpAddress());
            assertEquals("1.2.0.0/16", response.getNetwork().toString());

            DomainResponse tryResponse = reader.tryDomain(ipAddress).get();
            assertEquals(response.toJson(), tryResponse.toJson());
        }
    }

    @Test
    public void testEnterprise() throws Exception {
        try (DatabaseReader reader = new DatabaseReader.Builder(
            getFile("GeoIP2-Enterprise-Test.mmdb")).build()
        ) {
            InetAddress ipAddress = InetAddress.getByName("74.209.24.0");

            EnterpriseResponse response = reader.enterprise(ipAddress);
            assertEquals(11, response.getCity().getConfidence().intValue());
            assertEquals(99, response.getCountry().getConfidence().intValue());
            assertEquals(6252001, response.getCountry().getGeoNameId().intValue());
            assertEquals(27, response.getLocation().getAccuracyRadius().intValue());
            assertEquals(ConnectionType.CABLE_DSL, response.getTraits().getConnectionType());
            assertTrue(response.getTraits().isLegitimateProxy());
            assertEquals(ipAddress.getHostAddress(), response.getTraits().getIpAddress());
            assertEquals("74.209.16.0/20", response.getTraits().getNetwork().toString());

            EnterpriseResponse tryResponse = reader.tryEnterprise(ipAddress).get();
            assertEquals(response.toJson(), tryResponse.toJson());

            ipAddress = InetAddress.getByName("149.101.100.0");
            response = reader.enterprise(ipAddress);
            assertEquals("310", response.getTraits().getMobileCountryCode());
            assertEquals("004", response.getTraits().getMobileNetworkCode());

            // Test that the city and country methods can be called without
            // an exception
            reader.city(ipAddress);
            reader.country(ipAddress);
        }
    }

    @Test
    public void testIsp() throws Exception {
        try (DatabaseReader reader = new DatabaseReader.Builder(
            this.getFile("GeoIP2-ISP-Test.mmdb")).build()
        ) {
            InetAddress ipAddress = InetAddress.getByName("1.128.0.0");
            IspResponse response = reader.isp(ipAddress);
            assertEquals(1221, response.getAutonomousSystemNumber().intValue());
            assertEquals("Telstra Pty Ltd",
                response.getAutonomousSystemOrganization());
            assertEquals("Telstra Internet", response.getIsp());
            assertEquals("Telstra Internet", response.getOrganization());

            assertEquals(ipAddress.getHostAddress(), response.getIpAddress());
            assertEquals("1.128.0.0/11", response.getNetwork().toString());

            IspResponse tryResponse = reader.tryIsp(ipAddress).get();
            assertEquals(response.toJson(), tryResponse.toJson());

            ipAddress = InetAddress.getByName("149.101.100.0");
            response = reader.isp(ipAddress);
            assertEquals("310", response.getMobileCountryCode());
            assertEquals("004", response.getMobileNetworkCode());
        }
    }

    private File getFile(String filename) throws URISyntaxException {
        URL resource = DatabaseReaderTest.class
            .getResource("/maxmind-db/test-data/" + filename);
        return new File(resource.toURI());
    }
}
