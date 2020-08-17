package com.maxmind.geoip2;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.maxmind.db.*;
import com.maxmind.db.Reader.FileMode;
import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.*;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.InstantiationException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * The class {@code DatabaseReader} provides a reader for the GeoIP2 database
 * format.
 * </p>
 * <h2>Usage</h2>
 * <p>
 * To use the database API, you must create a new {@code DatabaseReader} using
 * the {@code DatabaseReader.Builder}. You must provide the {@code Builder}
 * constructor either an {@code InputStream} or {@code File} for your GeoIP2
 * database. You may also specify the {@code fileMode} and the {@code locales}
 * fallback order using the methods on the {@code Builder} object.
 * </p>
 * <p>
 * After you have created the {@code DatabaseReader}, you may then call one of
 * the appropriate methods, e.g., {@code city} or {@code tryCity}, for your
 * database. These methods take the IP address to be looked up.  The methods
 * with the "try" prefix return an {@code Optional} object, which will be
 * empty if the value is not present in the database. The method without the
 * prefix will throw an {@code AddressNotFoundException} if the address is
 * not in the database. If you are looking up many IPs that are not contained
 * in the database, the "try" method will be slightly faster as they do not
 * need to construct and throw an exception. These methods otherwise behave
 * the same.
 * </p>
 * <p>
 * If the lookup succeeds, the method call will return a response class for
 * the GeoIP2 lookup. The class in turn contains multiple record classes,
 * each of which represents part of the data returned by the database.
 * </p>
 * <p>
 * We recommend reusing the {@code DatabaseReader} object rather than creating
 * a new one for each lookup. The creation of this object is relatively
 * expensive as it must read in metadata for the file. It is safe to share the
 * object across threads.
 * </p>
 * <h3>Caching</h3>
 * <p>
 * The database API supports pluggable caching (by default, no caching is
 * performed). A simple implementation is provided by
 * {@code com.maxmind.db.CHMCache}.  Using this cache, lookup performance is
 * significantly improved at the cost of a small (~2MB) memory overhead.
 * </p>
 */
public class DatabaseReader implements DatabaseProvider, Closeable {

    private final Reader reader;

    private final ObjectMapper om;

    private final List<String> locales;

    private final int databaseType;

    private enum DatabaseType {
        ANONYMOUS_IP,
        ASN,
        CITY,
        CONNECTION_TYPE,
        COUNTRY,
        DOMAIN,
        ENTERPRISE,
        ISP;

        final int type;

        DatabaseType() {
            type = 1 << this.ordinal();
        }
    }

    private DatabaseReader(Builder builder)
            throws IOException,
                   InstantiationException,
                   IllegalAccessException,
                   InvocationTargetException,
                   NoSuchMethodException {
        if (builder.stream != null) {
            this.reader = new Reader(builder.stream, builder.cache);
        } else if (builder.database != null) {
            this.reader = new Reader(builder.database, builder.mode, builder.cache);
        } else {
            // This should never happen. If it does, review the Builder class
            // constructors for errors.
            throw new IllegalArgumentException(
                    "Unsupported Builder configuration: expected either File or URL");
        }
        this.om = new ObjectMapper();
        this.om.configure(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS, false);
        this.om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                false);
        this.om.configure(
                DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        this.locales = builder.locales;

        databaseType = getDatabaseType();
    }

    private int getDatabaseType() {
        String databaseType = this.getMetadata().getDatabaseType();
        int type = 0;
        if (databaseType.contains("GeoIP2-Anonymous-IP")) {
            type |= DatabaseType.ANONYMOUS_IP.type;
        }
        if (databaseType.contains("GeoLite2-ASN")) {
            type |= DatabaseType.ASN.type;
        }
        if (databaseType.contains("City")) {
            type |= DatabaseType.CITY.type | DatabaseType.COUNTRY.type;
        }
        if (databaseType.contains("GeoIP2-Connection-Type")) {
            type |= DatabaseType.CONNECTION_TYPE.type;
        }
        if (databaseType.contains("Country")) {
            type |= DatabaseType.COUNTRY.type;
        }
        if (databaseType.contains("GeoIP2-Domain")) {
            type |= DatabaseType.DOMAIN.type;
        }
        if (databaseType.contains("Enterprise")) {
            type |= DatabaseType.ENTERPRISE.type | DatabaseType.CITY.type | DatabaseType.COUNTRY.type;
        }
        if (databaseType.contains("GeoIP2-ISP")) {
            type |= DatabaseType.ISP.type;
        }
        if (type == 0) {
            // XXX - exception type
            throw new UnsupportedOperationException(
                    "Invalid attempt to open an unknown database type: " + databaseType);
        }
        return type;
    }

    /**
     * <p>
     * Constructs a Builder for the {@code DatabaseReader}. The file passed to
     * it must be a valid GeoIP2 database file.
     * </p>
     * <p>
     * {@code Builder} creates instances of {@code DatabaseReader}
     * from values set by the methods.
     * </p>
     * <p>
     * Only the values set in the {@code Builder} constructor are required.
     * </p>
     */
    public static final class Builder {
        final File database;
        final InputStream stream;

        List<String> locales = Collections.singletonList("en");
        FileMode mode = FileMode.MEMORY_MAPPED;
        NodeCache cache = NoCache.getInstance();

        /**
         * @param stream the stream containing the GeoIP2 database to use.
         */
        public Builder(InputStream stream) {
            this.stream = stream;
            this.database = null;
        }

        /**
         * @param database the GeoIP2 database file to use.
         */
        public Builder(File database) {
            this.database = database;
            this.stream = null;
        }

        /**
         * @param val List of locale codes to use in name property from most
         *            preferred to least preferred.
         * @return Builder object
         */
        public Builder locales(List<String> val) {
            this.locales = val;
            return this;
        }

        /**
         * @param cache backing cache instance
         * @return Builder object
         */
        public Builder withCache(NodeCache cache) {
            this.cache = cache;
            return this;
        }

        /**
         * @param val The file mode used to open the GeoIP2 database
         * @return Builder object
         * @throws java.lang.IllegalArgumentException if you initialized the Builder with a URL, which uses
         *                                            {@link FileMode#MEMORY}, but you provided a different
         *                                            FileMode to this method.
         */
        public Builder fileMode(FileMode val) {
            if (this.stream != null && FileMode.MEMORY != val) {
                throw new IllegalArgumentException(
                        "Only FileMode.MEMORY is supported when using an InputStream.");
            }
            this.mode = val;
            return this;
        }

        /**
         * @return an instance of {@code DatabaseReader} created from the
         * fields set on this builder.
         * @throws IOException if there is an error reading the database
         */
        public DatabaseReader build()
                throws IOException,
                       InstantiationException,
                       IllegalAccessException,
                       InvocationTargetException,
                       NoSuchMethodException {
            return new DatabaseReader(this);
        }
    }

    static final class LookupResult<T> {
        final T model;
        final String ipAddress;
        final Network network;

        LookupResult(T model, String ipAddress, Network network) {
            this.model = model;
            this.ipAddress = ipAddress;
            this.network = network;
        }

        T getModel() {
            return this.model;
        }

        String getIpAddress() {
            return this.ipAddress;
        }

        Network getNetwork() {
            return this.network;
        }
    }

    /**
     * @param ipAddress    IPv4 or IPv6 address to lookup.
     * @param cls          The class to deserialize to.
     * @param expectedType The expected database type.
     * @param stackDepth   Used to work out how far down the stack we should look, for the method name
     *                     we should use to report back to the user when in error. If this is called directly from the
     *                     method to report to the use set to zero, if this is called indirectly then it is the number of
     *                     methods between this method and the method to report the name of.
     * @return A LookupResult<T> object with the data for the IP address
     * @throws IOException if there is an error opening or reading from the file.
     */
    private <T> LookupResult<T> get(InetAddress ipAddress, Class<T> cls,
                                DatabaseType expectedType, int stackDepth)
            throws IOException,
                   AddressNotFoundException,
                   InstantiationException,
                   IllegalAccessException,
                   InvocationTargetException,
                   NoSuchMethodException {

        if ((databaseType & expectedType.type) == 0) {
            String caller = Thread.currentThread().getStackTrace()[2 + stackDepth]
                    .getMethodName();
            throw new UnsupportedOperationException(
                    "Invalid attempt to open a " + getMetadata().getDatabaseType()
                            + " database using the " + caller + " method");
        }

        // We are using the fully qualified name as otherwise it is ambiguous
        // on Java 14 due to the new java.lang.Record.
        com.maxmind.db.Record<T> record = reader.getRecord(ipAddress, cls);

        T o = cls.cast(record.getData());

        return new LookupResult<>(o, ipAddress.getHostAddress(), record.getNetwork());
    }

    /**
     * <p>
     * Closes the database.
     * </p>
     * <p>
     * If you are using {@code FileMode.MEMORY_MAPPED}, this will
     * <em>not</em> unmap the underlying file due to a limitation in Java's
     * {@code MappedByteBuffer}. It will however set the reference to
     * the buffer to {@code null}, allowing the garbage collector to
     * collect it.
     * </p>
     *
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void close() throws IOException {
        this.reader.close();
    }

    @Override
    public CountryResponse country(InetAddress ipAddress)
        throws IOException,
               GeoIp2Exception,
               InstantiationException,
               IllegalAccessException,
               InvocationTargetException,
               NoSuchMethodException {
        Optional<CountryResponse> r = getCountry(ipAddress, 1);
        if (!r.isPresent()) {
            throw new AddressNotFoundException("The address "
                    + ipAddress.getHostAddress() + " is not in the database.");
        }
        return r.get();
    }

    @Override
    public Optional<CountryResponse> tryCountry(InetAddress ipAddress)
        throws IOException,
               GeoIp2Exception,
               InstantiationException,
               IllegalAccessException,
               InvocationTargetException,
               NoSuchMethodException {
        return getCountry(ipAddress, 0);
    }

    private Optional<CountryResponse> getCountry(
            InetAddress ipAddress,
            int stackDepth
    ) throws IOException,
             GeoIp2Exception,
             InstantiationException,
             IllegalAccessException,
             InvocationTargetException,
             NoSuchMethodException {
        LookupResult<CountryDatabaseModel> result = this.get(
                ipAddress,
                CountryDatabaseModel.class,
                DatabaseType.COUNTRY,
                stackDepth
        );
        CountryDatabaseModel model = result.getModel();
        if (model == null) {
            return Optional.empty();
        }
        return Optional.of(
            new CountryResponse(
                model,
                result.getIpAddress(),
                result.getNetwork(),
                locales
            )
        );
    }

    @Override
    public CityResponse city(InetAddress ipAddress)
        throws IOException,
               GeoIp2Exception,
               InstantiationException,
               IllegalAccessException,
               InvocationTargetException,
               NoSuchMethodException {
        Optional<CityResponse> r = getCity(ipAddress, 1);
        if (!r.isPresent()) {
            throw new AddressNotFoundException("The address "
                    + ipAddress.getHostAddress() + " is not in the database.");
        }
        return r.get();
    }

    @Override
    public Optional<CityResponse> tryCity(InetAddress ipAddress)
        throws IOException,
               GeoIp2Exception,
               InstantiationException,
               IllegalAccessException,
               InvocationTargetException,
               NoSuchMethodException {
        return getCity(ipAddress, 0);
    }

    private Optional<CityResponse> getCity(
            InetAddress ipAddress,
            int stackDepth
    ) throws IOException,
             GeoIp2Exception,
             InstantiationException,
             IllegalAccessException,
             InvocationTargetException,
             NoSuchMethodException {
        LookupResult<CityDatabaseModel> result = this.get(
                ipAddress,
                CityDatabaseModel.class,
                DatabaseType.CITY,
                stackDepth
        );
        CityDatabaseModel model = result.getModel();
        if (model == null) {
            return Optional.empty();
        }
        return Optional.of(
            new CityResponse(
                model,
                result.getIpAddress(),
                result.getNetwork(),
                locales
            )
        );
    }

    /**
     * Look up an IP address in a GeoIP2 Anonymous IP.
     *
     * @param ipAddress IPv4 or IPv6 address to lookup.
     * @return a AnonymousIpResponse for the requested IP address.
     * @throws GeoIp2Exception if there is an error looking up the IP
     * @throws IOException     if there is an IO error
     */
    @Override
    public AnonymousIpResponse anonymousIp(InetAddress ipAddress)
        throws IOException,
               GeoIp2Exception,
               InstantiationException,
               IllegalAccessException,
               InvocationTargetException,
               NoSuchMethodException {
        Optional<AnonymousIpResponse> r = getAnonymousIp(ipAddress, 1);
        if (!r.isPresent()) {
            throw new AddressNotFoundException("The address "
                    + ipAddress.getHostAddress() + " is not in the database.");
        }
        return r.get();
    }

    @Override
    public Optional<AnonymousIpResponse> tryAnonymousIp(InetAddress ipAddress)
        throws IOException,
               GeoIp2Exception,
               InstantiationException,
               IllegalAccessException,
               InvocationTargetException,
               NoSuchMethodException {
        return getAnonymousIp(ipAddress, 0);
    }

    private Optional<AnonymousIpResponse> getAnonymousIp(
            InetAddress ipAddress,
            int stackDepth
    ) throws IOException,
             GeoIp2Exception,
             InstantiationException,
             IllegalAccessException,
             InvocationTargetException,
             NoSuchMethodException {
        LookupResult<AnonymousIpDatabaseModel> result = this.get(
                ipAddress,
                AnonymousIpDatabaseModel.class,
                DatabaseType.ANONYMOUS_IP,
                stackDepth
        );
        AnonymousIpDatabaseModel model = result.getModel();
        if (model == null) {
            return Optional.empty();
        }
        return Optional.of(
            new AnonymousIpResponse(
                model,
                result.getIpAddress(),
                result.getNetwork()
            )
        );
    }

    /**
     * Look up an IP address in a GeoLite2 ASN database.
     *
     * @param ipAddress IPv4 or IPv6 address to lookup.
     * @return an AsnResponse for the requested IP address.
     * @throws GeoIp2Exception if there is an error looking up the IP
     * @throws IOException     if there is an IO error
     */
    @Override
    public AsnResponse asn(InetAddress ipAddress)
        throws IOException,
               GeoIp2Exception,
               InstantiationException,
               IllegalAccessException,
               InvocationTargetException,
               NoSuchMethodException {
        Optional<AsnResponse> r = getAsn(ipAddress, 1);
        if (!r.isPresent()) {
            throw new AddressNotFoundException("The address "
                    + ipAddress.getHostAddress() + " is not in the database.");
        }
        return r.get();
    }

    @Override
    public Optional<AsnResponse> tryAsn(InetAddress ipAddress)
        throws IOException,
               GeoIp2Exception,
               InstantiationException,
               IllegalAccessException,
               InvocationTargetException,
               NoSuchMethodException {
        return getAsn(ipAddress, 0);
    }

    private Optional<AsnResponse> getAsn(InetAddress ipAddress, int stackDepth)
        throws IOException,
               GeoIp2Exception,
               InstantiationException,
               IllegalAccessException,
               InvocationTargetException,
               NoSuchMethodException {
        LookupResult<AsnDatabaseModel> result = this.get(
                ipAddress,
                AsnDatabaseModel.class,
                DatabaseType.ASN,
                stackDepth
        );
        AsnDatabaseModel model = result.getModel();
        if (model == null) {
            return Optional.empty();
        }
        return Optional.of(
            new AsnResponse(
                model,
                result.getIpAddress(),
                result.getNetwork()
            )
        );
    }

    /**
     * Look up an IP address in a GeoIP2 Connection Type database.
     *
     * @param ipAddress IPv4 or IPv6 address to lookup.
     * @return a ConnectTypeResponse for the requested IP address.
     * @throws GeoIp2Exception if there is an error looking up the IP
     * @throws IOException     if there is an IO error
     */
    @Override
    public ConnectionTypeResponse connectionType(InetAddress ipAddress)
        throws IOException,
               GeoIp2Exception,
               InstantiationException,
               IllegalAccessException,
               InvocationTargetException,
               NoSuchMethodException {
        Optional<ConnectionTypeResponse> r = getConnectionType(ipAddress, 1);
        if (!r.isPresent()) {
            throw new AddressNotFoundException("The address "
                    + ipAddress.getHostAddress() + " is not in the database.");
        }
        return r.get();
    }

    @Override
    public Optional<ConnectionTypeResponse> tryConnectionType(InetAddress ipAddress)
        throws IOException,
               GeoIp2Exception,
               InstantiationException,
               IllegalAccessException,
               InvocationTargetException,
               NoSuchMethodException {
        return getConnectionType(ipAddress, 0);
    }

    private Optional<ConnectionTypeResponse> getConnectionType(
            InetAddress ipAddress,
            int stackDepth
    ) throws IOException,
             GeoIp2Exception,
             InstantiationException,
             IllegalAccessException,
             InvocationTargetException,
             NoSuchMethodException {
        LookupResult<ConnectionTypeDatabaseModel> result = this.get(
                ipAddress,
                ConnectionTypeDatabaseModel.class,
                DatabaseType.CONNECTION_TYPE,
                stackDepth
        );
        ConnectionTypeDatabaseModel model = result.getModel();
        if (model == null) {
            return Optional.empty();
        }
        return Optional.of(
            new ConnectionTypeResponse(
                model,
                result.getIpAddress(),
                result.getNetwork()
            )
        );
    }

    /**
     * Look up an IP address in a GeoIP2 Domain database.
     *
     * @param ipAddress IPv4 or IPv6 address to lookup.
     * @return a DomainResponse for the requested IP address.
     * @throws GeoIp2Exception if there is an error looking up the IP
     * @throws IOException     if there is an IO error
     */
    @Override
    public DomainResponse domain(InetAddress ipAddress)
        throws IOException,
               GeoIp2Exception,
               InstantiationException,
               IllegalAccessException,
               InvocationTargetException,
               NoSuchMethodException {
        Optional<DomainResponse> r = getDomain(ipAddress, 1);
        if (!r.isPresent()) {
            throw new AddressNotFoundException("The address "
                    + ipAddress.getHostAddress() + " is not in the database.");
        }
        return r.get();
    }

    @Override
    public Optional<DomainResponse> tryDomain(InetAddress ipAddress)
        throws IOException,
               GeoIp2Exception,
               InstantiationException,
               IllegalAccessException,
               InvocationTargetException,
               NoSuchMethodException {
        return getDomain(ipAddress, 0);
    }

    private Optional<DomainResponse> getDomain(
            InetAddress ipAddress,
            int stackDepth
    ) throws IOException,
             GeoIp2Exception,
             InstantiationException,
             IllegalAccessException,
             InvocationTargetException,
             NoSuchMethodException {
        LookupResult<DomainDatabaseModel> result = this.get(
                ipAddress,
                DomainDatabaseModel.class,
                DatabaseType.DOMAIN,
                stackDepth
        );
        DomainDatabaseModel model = result.getModel();
        if (model == null) {
            return Optional.empty();
        }
        return Optional.of(
            new DomainResponse(
                model,
                result.getIpAddress(),
                result.getNetwork()
            )
        );
    }

    /**
     * Look up an IP address in a GeoIP2 Enterprise database.
     *
     * @param ipAddress IPv4 or IPv6 address to lookup.
     * @return an EnterpriseResponse for the requested IP address.
     * @throws GeoIp2Exception if there is an error looking up the IP
     * @throws IOException     if there is an IO error
     */
    @Override
    public EnterpriseResponse enterprise(InetAddress ipAddress)
        throws IOException,
               GeoIp2Exception,
               InstantiationException,
               IllegalAccessException,
               InvocationTargetException,
               NoSuchMethodException {
        Optional<EnterpriseResponse> r = getEnterprise(ipAddress, 1);
        if (!r.isPresent()) {
            throw new AddressNotFoundException("The address "
                    + ipAddress.getHostAddress() + " is not in the database.");
        }
        return r.get();
    }

    @Override
    public Optional<EnterpriseResponse> tryEnterprise(InetAddress ipAddress)
        throws IOException,
               GeoIp2Exception,
               InstantiationException,
               IllegalAccessException,
               InvocationTargetException,
               NoSuchMethodException {
        return getEnterprise(ipAddress, 0);
    }

    private Optional<EnterpriseResponse> getEnterprise(
            InetAddress ipAddress,
            int stackDepth
    ) throws IOException,
             GeoIp2Exception,
             InstantiationException,
             IllegalAccessException,
             InvocationTargetException,
             NoSuchMethodException {
        LookupResult<CityDatabaseModel> result = this.get(
                ipAddress,
                CityDatabaseModel.class,
                DatabaseType.ENTERPRISE,
                stackDepth
        );
        CityDatabaseModel model = result.getModel();
        if (model == null) {
            return Optional.empty();
        }
        return Optional.of(
            new EnterpriseResponse(
                model,
                result.getIpAddress(),
                result.getNetwork(),
                locales
            )
        );
    }

    /**
     * Look up an IP address in a GeoIP2 ISP database.
     *
     * @param ipAddress IPv4 or IPv6 address to lookup.
     * @return an IspResponse for the requested IP address.
     * @throws GeoIp2Exception if there is an error looking up the IP
     * @throws IOException     if there is an IO error
     */
    @Override
    public IspResponse isp(InetAddress ipAddress)
        throws IOException,
               GeoIp2Exception,
               InstantiationException,
               IllegalAccessException,
               InvocationTargetException,
               NoSuchMethodException {
        Optional<IspResponse> r = getIsp(ipAddress, 1);
        if (!r.isPresent()) {
            throw new AddressNotFoundException("The address "
                    + ipAddress.getHostAddress() + " is not in the database.");
        }
        return r.get();
    }

    @Override
    public Optional<IspResponse> tryIsp(InetAddress ipAddress)
        throws IOException,
               GeoIp2Exception,
               InstantiationException,
               IllegalAccessException,
               InvocationTargetException,
               NoSuchMethodException {
        return getIsp(ipAddress, 0);
    }

    private Optional<IspResponse> getIsp(
            InetAddress ipAddress,
            int stackDepth
    ) throws IOException,
             GeoIp2Exception,
             InstantiationException,
             IllegalAccessException,
             InvocationTargetException,
             NoSuchMethodException {
        LookupResult<IspDatabaseModel> result = this.get(
                ipAddress,
                IspDatabaseModel.class,
                DatabaseType.ISP,
                stackDepth
        );
        IspDatabaseModel model = result.getModel();
        if (model == null) {
            return Optional.empty();
        }
        return Optional.of(
            new IspResponse(
                model,
                result.getIpAddress(),
                result.getNetwork()
            )
        );
    }

    /**
     * @return the metadata for the open MaxMind DB file.
     */
    public Metadata getMetadata() {
        return this.reader.getMetadata();
    }
}
