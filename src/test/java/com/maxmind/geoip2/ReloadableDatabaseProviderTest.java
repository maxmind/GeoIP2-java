/* HEADER */
package com.maxmind.geoip2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Before;
import org.junit.Test;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;

public class ReloadableDatabaseProviderTest {
    private static final String TEST_IP_ADDRESS = "81.2.69.160";
    private File geoipFile;

    @Before
    public void setup() throws URISyntaxException, IOException {
        URL resource = DatabaseReaderTest.class.getResource("/maxmind-db/test-data/GeoIP2-City-Test.mmdb");
        geoipFile = new File(resource.toURI());
    }

    @Test(expected = IOException.class)
    public void eagerLoadingFileDoesNotExist() throws IOException, GeoIp2Exception {
        File unknownFile = new File("unknown.mmdb");
        DatabaseReader.Builder builder = new DatabaseReader.Builder(unknownFile);

        try (DatabaseProvider provider = new ReloadableDatabaseProvider(builder, unknownFile)) {
            fail("An exception should have been thrown!");
        }
    }

    @Test
    public void eagerLoadingFileExists() throws IOException, GeoIp2Exception {
        DatabaseReader.Builder builder = new DatabaseReader.Builder(geoipFile);
        DatabaseProvider reader = new ReloadableDatabaseProvider(builder, geoipFile);
        testDefaultLocale(reader);
    }

    @Test
    public void eagerLoadingReload() throws IOException, GeoIp2Exception {
        DatabaseReader.Builder builder = new DatabaseReader.Builder(geoipFile);
        Callable<Boolean> availableChecker = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return true; // always report there are updates
            }
        };
        // reader is eagerly initialized
        DatabaseProvider reader = new ReloadableDatabaseProvider(builder, availableChecker, false);
        // requesting look up will force reader to be reloaded
        testDefaultLocale(reader);
    }

    @Test
    public void eagerLoadingReloadNotAvailable() throws IOException, GeoIp2Exception {
        DatabaseReader.Builder builder = new DatabaseReader.Builder(geoipFile);
        final AtomicBoolean available = new AtomicBoolean(true);
        Callable<Boolean> availableChecker = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (available.get()) {
                    // only report available once, then not available
                    available.set(false);
                    return true;
                }
                return available.get();
            }
        };

        DatabaseProvider reader = new ReloadableDatabaseProvider(builder, availableChecker, false);
        testDefaultLocale(reader);
    }

    @Test
    public void lazyLoadingFileDoesNotExist() throws IOException, GeoIp2Exception {
        File unknownFile = new File("unknown.mmdb");
        DatabaseReader.Builder builder = new DatabaseReader.Builder(unknownFile);

        try (DatabaseProvider reader = new ReloadableDatabaseProvider(builder, unknownFile, true)) {
            // file should not be accessed yet
        }
    }

    @Test
    public void lazyLoadingFileExists() throws IOException, GeoIp2Exception {
        DatabaseReader.Builder builder = new DatabaseReader.Builder(geoipFile);
        ReloadableDatabaseProvider reader = new ReloadableDatabaseProvider(builder, geoipFile, true);
        testDefaultLocale(reader);
    }

    @Test(expected = IOException.class)
    public void databaseNotReady() throws IOException, GeoIp2Exception {
        DatabaseReader.Builder builder = new DatabaseReader.Builder(geoipFile);
        Callable<Boolean> availableChecker = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return false;
            }
        };
        assertFailedRead(builder, availableChecker, true);
    }

    @Test(expected = IOException.class)
    public void databaseLoadFails() throws IOException, GeoIp2Exception {
        DatabaseReader.Builder builder = new DatabaseReader.Builder(geoipFile);
        Callable<Boolean> availableChecker = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                throw new IllegalArgumentException("Database cannot be loaded");
            }
        };
        assertFailedRead(builder, availableChecker, true);
    }

    private void testDefaultLocale(DatabaseProvider reader) throws IOException, GeoIp2Exception {
        CityResponse city = reader.city(InetAddress.getByName(TEST_IP_ADDRESS));
        assertEquals("London", city.getCity().getName());
        reader.close();
    }

    private void assertFailedRead(DatabaseReader.Builder builder, Callable<Boolean> availableChecker, boolean lazyInitialization)
                    throws IOException, GeoIp2Exception, UnknownHostException {
        try (ReloadableDatabaseProvider reader = new ReloadableDatabaseProvider(builder, availableChecker, lazyInitialization)) {
            reader.city(InetAddress.getByName(TEST_IP_ADDRESS));
            fail("An exception should have been thrown!");
        }
    }
}