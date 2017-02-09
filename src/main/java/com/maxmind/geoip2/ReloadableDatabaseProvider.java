/* HEADER */
package com.maxmind.geoip2;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import com.maxmind.db.Metadata;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.AnonymousIpResponse;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.ConnectionTypeResponse;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.model.DomainResponse;
import com.maxmind.geoip2.model.EnterpriseResponse;
import com.maxmind.geoip2.model.IspResponse;

/**
 * Implementation of {@link DatabaseProvider} that allows the {@link DatabaseReader} to be rebuilt when the database is updated.
 */
public class ReloadableDatabaseProvider implements DatabaseProvider {
    private final ReentrantLock lock = new ReentrantLock();
    private final DatabaseReader.Builder builder;
    private final Callable<Boolean> availableChecker;
    private DatabaseProvider databaseProvider;

    /**
     * Construct a {@link ReloadableDatabaseProvider} which initialises its reader eagerly.
     *
     * @param builder
     * @param databaseFile
     * @throws IOException
     */
    public ReloadableDatabaseProvider(DatabaseReader.Builder builder, File databaseFile) throws IOException {
        this(builder, new DatabaseFileAvailableChecker(databaseFile), false);
    }

    /**
     * Construct a {@link ReloadableDatabaseProvider}. This constructor provides a lazy initialization option, that allows a client
     * to start an application with no database file whilst this can be downloaded in another thread.
     *
     * @param builder
     * @param databaseFile
     * @param lazyInitialization
     * @throws IOException
     */
    public ReloadableDatabaseProvider(DatabaseReader.Builder builder, File databaseFile, boolean lazyInitialization)
        throws IOException {
        this(builder, new DatabaseFileAvailableChecker(databaseFile), true);
    }

    /**
     * Construct a {@link ReloadableDatabaseProvider}. This constructor provides options to check if the database is available and
     * also lazy initialisation. Lazy initialisation allows a client to start an application with no database file whilst this can
     * be downloaded in another thread.
     *
     * @param builder
     * @param availableChecker
     * @param lazyInitialization
     * @throws IOException
     */
    public ReloadableDatabaseProvider(DatabaseReader.Builder builder, Callable<Boolean> availableChecker,
                                      boolean lazyInitialization)
        throws IOException {
        this.builder = builder;
        this.availableChecker = availableChecker;

        if (!lazyInitialization) {
            initialise();
        }
    }

    @Override
    public CountryResponse country(InetAddress ipAddress) throws IOException, GeoIp2Exception {
        return getProvider().country(ipAddress);
    }

    @Override
    public CityResponse city(InetAddress ipAddress) throws IOException, GeoIp2Exception {
        return getProvider().city(ipAddress);
    }

    @Override
    public AnonymousIpResponse anonymousIp(InetAddress ipAddress) throws IOException, GeoIp2Exception {
        return getProvider().anonymousIp(ipAddress);
    }

    @Override
    public ConnectionTypeResponse connectionType(InetAddress ipAddress) throws IOException, GeoIp2Exception {
        return getProvider().connectionType(ipAddress);
    }

    @Override
    public DomainResponse domain(InetAddress ipAddress) throws IOException, GeoIp2Exception {
        return getProvider().domain(ipAddress);
    }

    @Override
    public EnterpriseResponse enterprise(InetAddress ipAddress) throws IOException, GeoIp2Exception {
        return getProvider().enterprise(ipAddress);
    }

    @Override
    public IspResponse isp(InetAddress ipAddress) throws IOException, GeoIp2Exception {
        return getProvider().isp(ipAddress);
    }

    private DatabaseProvider getProvider() throws IOException {
        initialise();
        return databaseProvider;
    }

    private void initialise() throws IOException {
        try {
            boolean tryLock = lock.tryLock(500L, TimeUnit.MILLISECONDS);
            if (tryLock && availableChecker.call()) {
                close();
                databaseProvider = new DatabaseReader(builder);
            }

            if (databaseProvider == null) {
                throw new IOException("Reader has not been constructed, database not available!");
            }
        } catch (Exception expt) {
            throw new IOException("Unable to initialise database!", expt);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Metadata getMetadata() throws IOException {
        return getProvider().getMetadata();
    }

    @Override
    public void close() throws IOException {
        if (databaseProvider != null) {
            databaseProvider.close();
        }
    }

    /**
     * Implementation of {@link Callable} that returns <tt>true</tt> if the supplied database {@link File} exists or has been
     * modified.
     */
    public static class DatabaseFileAvailableChecker implements Callable<Boolean> {
        private final File database;
        private Long lastModified;

        public DatabaseFileAvailableChecker(File database) {
            this.database = database;
        }

        @Override
        public Boolean call() throws Exception {
            if (!database.exists()) {
                return false;
            }

            long updatedLastModified = database.lastModified();
            if (lastModified != null && lastModified.equals(updatedLastModified)) {
                return false;
            }
            lastModified = database.lastModified();
            return true;
        }
    }
}