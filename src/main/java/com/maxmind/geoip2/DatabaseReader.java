package com.maxmind.geoip2;

import com.maxmind.db.DatabaseRecord;
import com.maxmind.db.Metadata;
import com.maxmind.db.Network;
import com.maxmind.db.NoCache;
import com.maxmind.db.NodeCache;
import com.maxmind.db.Reader;
import com.maxmind.db.Reader.FileMode;
import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.AnonymousIpResponse;
import com.maxmind.geoip2.model.AnonymousPlusResponse;
import com.maxmind.geoip2.model.AsnResponse;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.ConnectionTypeResponse;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.model.DomainResponse;
import com.maxmind.geoip2.model.EnterpriseResponse;
import com.maxmind.geoip2.model.IpRiskResponse;
import com.maxmind.geoip2.model.IspResponse;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
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

    private final List<String> locales;

    private final int databaseType;

    private enum DatabaseType {
        ANONYMOUS_IP,
        ANONYMOUS_PLUS,
        ASN,
        CITY,
        CONNECTION_TYPE,
        COUNTRY,
        DOMAIN,
        ENTERPRISE,
        IP_RISK,
        ISP;

        final int type;

        DatabaseType() {
            type = 1 << this.ordinal();
        }
    }

    private DatabaseReader(Builder builder) throws IOException {
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
        this.locales = builder.locales;

        databaseType = getDatabaseType();
    }

    private int getDatabaseType() {
        String databaseType = this.metadata().databaseType();
        int type = 0;
        if (databaseType.contains("GeoIP2-Anonymous-IP")) {
            type |= DatabaseType.ANONYMOUS_IP.type;
        }
        if (databaseType.contains("GeoIP-Anonymous-Plus")) {
            type |= DatabaseType.ANONYMOUS_PLUS.type;
        }
        if (databaseType.contains("GeoIP2-IP-Risk")) {
            type |= DatabaseType.IP_RISK.type;
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
            type |=
                DatabaseType.ENTERPRISE.type | DatabaseType.CITY.type | DatabaseType.COUNTRY.type;
        }
        if (databaseType.contains("GeoIP2-ISP")) {
            type |= DatabaseType.ISP.type;
        }
        if (type == 0) {
            throw new IllegalArgumentException(
                "Unsupported database type: " + databaseType);
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

        List<String> locales = List.of("en");
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
         * @throws java.lang.IllegalArgumentException if you initialized the Builder with a URL,
         *                                            which uses {@link FileMode#MEMORY}, but you
         *                                            provided a different FileMode to this method.
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
        public DatabaseReader build() throws IOException {
            return new DatabaseReader(this);
        }
    }

    static record LookupResult<T>(T model, String ipAddress, Network network) {
    }

    /**
     * @param ipAddress    IPv4 or IPv6 address to lookup.
     * @param cls          The class to deserialize to.
     * @param expectedType The expected database type.
     * @param caller       The name of the public method calling this (for error messages).
     * @return A {@code LookupResult<T>} object with the data for the IP address
     * @throws IOException if there is an error opening or reading from the file.
     */
    private <T> LookupResult<T> get(InetAddress ipAddress, Class<T> cls,
                                    DatabaseType expectedType, String caller)
        throws IOException {

        if ((databaseType & expectedType.type) == 0) {
            throw new UnsupportedOperationException(
                "Invalid attempt to open a " + metadata().databaseType()
                    + " database using the " + caller + " method");
        }

        DatabaseRecord<T> record = reader.getRecord(ipAddress, cls);

        T o = record.data();

        return new LookupResult<>(o, ipAddress.getHostAddress(), record.network());
    }

    /**
     * Generic method to get a response.
     *
     * @param ipAddress    IPv4 or IPv6 address to lookup.
     * @param cls          The class to deserialize to.
     * @param expectedType The expected database type.
     * @param caller       The name of the public method calling this (for error messages).
     * @return An Optional containing the response, or empty if not found
     * @throws IOException if there is an error opening or reading from the file.
     */
    private <T> Optional<T> getResponse(
        InetAddress ipAddress,
        Class<T> cls,
        DatabaseType expectedType,
        String caller
    ) throws IOException {
        LookupResult<T> result = this.get(ipAddress, cls, expectedType, caller);
        T response = result.model();
        if (response == null) {
            return Optional.empty();
        }
        return Optional.of(response);
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
    public CountryResponse country(InetAddress ipAddress) throws IOException,
        GeoIp2Exception {
        Optional<CountryResponse> r = tryCountry(ipAddress);
        if (r.isEmpty()) {
            throw new AddressNotFoundException("The address "
                + ipAddress.getHostAddress() + " is not in the database.");
        }
        return r.get();
    }

    @Override
    public Optional<CountryResponse> tryCountry(InetAddress ipAddress) throws IOException,
        GeoIp2Exception {
        Optional<CountryResponse> response = getResponse(
            ipAddress,
            CountryResponse.class,
            DatabaseType.COUNTRY,
            "country"
        );
        return response.map(r -> new CountryResponse(r, locales));
    }

    @Override
    public CityResponse city(InetAddress ipAddress) throws IOException,
        GeoIp2Exception {
        Optional<CityResponse> r = tryCity(ipAddress);
        if (r.isEmpty()) {
            throw new AddressNotFoundException("The address "
                + ipAddress.getHostAddress() + " is not in the database.");
        }
        return r.get();
    }

    @Override
    public Optional<CityResponse> tryCity(InetAddress ipAddress) throws IOException,
        GeoIp2Exception {
        Optional<CityResponse> response = getResponse(
            ipAddress,
            CityResponse.class,
            DatabaseType.CITY,
            "city"
        );
        return response.map(r -> new CityResponse(r, locales));
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
    public AnonymousIpResponse anonymousIp(InetAddress ipAddress) throws IOException,
        GeoIp2Exception {
        Optional<AnonymousIpResponse> r = tryAnonymousIp(ipAddress);
        if (r.isEmpty()) {
            throw new AddressNotFoundException("The address "
                + ipAddress.getHostAddress() + " is not in the database.");
        }
        return r.get();
    }

    @Override
    public Optional<AnonymousIpResponse> tryAnonymousIp(InetAddress ipAddress) throws IOException,
        GeoIp2Exception {
        return getResponse(
            ipAddress,
            AnonymousIpResponse.class,
            DatabaseType.ANONYMOUS_IP,
            "anonymousIp"
        );
    }

    /**
     * Look up an IP address in a GeoIP2 Anonymous Plus.
     *
     * @param ipAddress IPv4 or IPv6 address to lookup.
     * @return a AnonymousPlusResponse for the requested IP address.
     * @throws GeoIp2Exception if there is an error looking up the IP
     * @throws IOException     if there is an IO error
     */
    @Override
    public AnonymousPlusResponse anonymousPlus(InetAddress ipAddress) throws IOException,
        GeoIp2Exception {
        Optional<AnonymousPlusResponse> r = tryAnonymousPlus(ipAddress);
        if (r.isEmpty()) {
            throw new AddressNotFoundException("The address "
                + ipAddress.getHostAddress() + " is not in the database.");
        }
        return r.get();
    }

    @Override
    public Optional<AnonymousPlusResponse> tryAnonymousPlus(InetAddress ipAddress)
        throws IOException,
        GeoIp2Exception {
        return getResponse(
            ipAddress,
            AnonymousPlusResponse.class,
            DatabaseType.ANONYMOUS_PLUS,
            "anonymousPlus"
        );
    }


    /**
     * Look up an IP address in a GeoIP2 IP Risk database.
     *
     * @param ipAddress IPv4 or IPv6 address to lookup.
     * @return a IPRiskResponse for the requested IP address.
     * @throws GeoIp2Exception if there is an error looking up the IP
     * @throws IOException     if there is an IO error
     */
    @Override
    public IpRiskResponse ipRisk(InetAddress ipAddress) throws IOException,
        GeoIp2Exception {
        Optional<IpRiskResponse> r = tryIpRisk(ipAddress);
        if (r.isEmpty()) {
            throw new AddressNotFoundException("The address "
                + ipAddress.getHostAddress() + " is not in the database.");
        }
        return r.get();
    }

    @Override
    public Optional<IpRiskResponse> tryIpRisk(InetAddress ipAddress) throws IOException,
        GeoIp2Exception {
        return getResponse(ipAddress, IpRiskResponse.class, DatabaseType.IP_RISK, "ipRisk");
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
    public AsnResponse asn(InetAddress ipAddress) throws IOException,
        GeoIp2Exception {
        Optional<AsnResponse> r = tryAsn(ipAddress);
        if (r.isEmpty()) {
            throw new AddressNotFoundException("The address "
                + ipAddress.getHostAddress() + " is not in the database.");
        }
        return r.get();
    }

    @Override
    public Optional<AsnResponse> tryAsn(InetAddress ipAddress) throws IOException,
        GeoIp2Exception {
        return getResponse(ipAddress, AsnResponse.class, DatabaseType.ASN, "asn");
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
        throws IOException, GeoIp2Exception {
        Optional<ConnectionTypeResponse> r = tryConnectionType(ipAddress);
        if (r.isEmpty()) {
            throw new AddressNotFoundException("The address "
                + ipAddress.getHostAddress() + " is not in the database.");
        }
        return r.get();
    }

    @Override
    public Optional<ConnectionTypeResponse> tryConnectionType(InetAddress ipAddress)
        throws IOException, GeoIp2Exception {
        return getResponse(
            ipAddress,
            ConnectionTypeResponse.class,
            DatabaseType.CONNECTION_TYPE,
            "connectionType"
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
    public DomainResponse domain(InetAddress ipAddress) throws IOException,
        GeoIp2Exception {
        Optional<DomainResponse> r = tryDomain(ipAddress);
        if (r.isEmpty()) {
            throw new AddressNotFoundException("The address "
                + ipAddress.getHostAddress() + " is not in the database.");
        }
        return r.get();
    }

    @Override
    public Optional<DomainResponse> tryDomain(InetAddress ipAddress) throws IOException,
        GeoIp2Exception {
        return getResponse(ipAddress, DomainResponse.class, DatabaseType.DOMAIN, "domain");
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
    public EnterpriseResponse enterprise(InetAddress ipAddress) throws IOException,
        GeoIp2Exception {
        Optional<EnterpriseResponse> r = tryEnterprise(ipAddress);
        if (r.isEmpty()) {
            throw new AddressNotFoundException("The address "
                + ipAddress.getHostAddress() + " is not in the database.");
        }
        return r.get();
    }

    @Override
    public Optional<EnterpriseResponse> tryEnterprise(InetAddress ipAddress) throws IOException,
        GeoIp2Exception {
        Optional<EnterpriseResponse> response = getResponse(
            ipAddress,
            EnterpriseResponse.class,
            DatabaseType.ENTERPRISE,
            "enterprise"
        );
        return response.map(r -> new EnterpriseResponse(r, locales));
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
    public IspResponse isp(InetAddress ipAddress) throws IOException,
        GeoIp2Exception {
        Optional<IspResponse> r = tryIsp(ipAddress);
        if (r.isEmpty()) {
            throw new AddressNotFoundException("The address "
                + ipAddress.getHostAddress() + " is not in the database.");
        }
        return r.get();
    }

    @Override
    public Optional<IspResponse> tryIsp(InetAddress ipAddress) throws IOException,
        GeoIp2Exception {
        return getResponse(ipAddress, IspResponse.class, DatabaseType.ISP, "isp");
    }

    /**
     * @return the metadata for the open MaxMind DB file.
     */
    public Metadata metadata() {
        return this.reader.getMetadata();
    }

    /**
     * @return the metadata for the open MaxMind DB file.
     * @deprecated Use {@link #metadata()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    public Metadata getMetadata() {
        return metadata();
    }
}
