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
        public DatabaseReader build() throws IOException {
            return new DatabaseReader(this);
        }
    }

    /**
     * @param ipAddress    IPv4 or IPv6 address to lookup.
     * @param cls          The class to deserialize to.
     * @param expectedType The expected database type.
     * @return A <T> object with the data for the IP address
     * @throws IOException              if there is an error opening or reading from the file.
     * @throws AddressNotFoundException if the IP address is not in our database
     */
    private <T> T getOrThrowException(InetAddress ipAddress, Class<T> cls,
                                      DatabaseType expectedType) throws IOException, AddressNotFoundException {
        Optional<T> t = get(ipAddress, cls, expectedType, 1);
        if (!t.isPresent()) {
            throw new AddressNotFoundException("The address "
                    + ipAddress.getHostAddress() + " is not in the database.");
        }

        return t.get();
    }

    /**
     * @param ipAddress    IPv4 or IPv6 address to lookup.
     * @param cls          The class to deserialize to.
     * @param expectedType The expected database type.
     * @param stackDepth   Used to work out how far down the stack we should look, for the method name
     *                     we should use to report back to the user when in error. If this is called directly from the
     *                     method to report to the use set to zero, if this is called indirectly then it is the number of
     *                     methods between this method and the method to report the name of.
     * @return A <T> object with the data for the IP address or null if the IP address is not in our database
     * @throws IOException if there is an error opening or reading from the file.
     */
    private <T> Optional<T> get(InetAddress ipAddress, Class<T> cls,
                                DatabaseType expectedType, int stackDepth) throws IOException, AddressNotFoundException {

        if ((databaseType & expectedType.type) == 0) {
            String caller = Thread.currentThread().getStackTrace()[2 + stackDepth]
                    .getMethodName();
            throw new UnsupportedOperationException(
                    "Invalid attempt to open a " + getMetadata().getDatabaseType()
                            + " database using the " + caller + " method");
        }

        ObjectNode node = jsonNodeToObjectNode(reader.get(ipAddress));

        // We throw the same exception as the web service when an IP is not in
        // the database
        if (node == null) {
            return Optional.empty();
        }

        InjectableValues inject = new JsonInjector(locales, ipAddress.getHostAddress());

        return Optional.of(this.om.reader(inject).treeToValue(node, cls));
    }


    private ObjectNode jsonNodeToObjectNode(JsonNode node)
            throws InvalidDatabaseException {
        if (node == null || node instanceof ObjectNode) {
            return (ObjectNode) node;
        }
        throw new InvalidDatabaseException(
                "Unexpected data type returned. The GeoIP2 database may be corrupt.");
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
        return this.getOrThrowException(ipAddress, CountryResponse.class, DatabaseType.COUNTRY);
    }

    @Override
    public Optional<CountryResponse> tryCountry(InetAddress ipAddress) throws IOException,
            GeoIp2Exception {
        return this.get(ipAddress, CountryResponse.class, DatabaseType.COUNTRY, 0);
    }

    @Override
    public CityResponse city(InetAddress ipAddress) throws IOException,
            GeoIp2Exception {
        return this.getOrThrowException(ipAddress, CityResponse.class, DatabaseType.CITY);
    }

    @Override
    public Optional<CityResponse> tryCity(InetAddress ipAddress) throws IOException,
            GeoIp2Exception {
        return this.get(ipAddress, CityResponse.class, DatabaseType.CITY, 0);
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
        return this.getOrThrowException(ipAddress, AnonymousIpResponse.class, DatabaseType.ANONYMOUS_IP);
    }

    @Override
    public Optional<AnonymousIpResponse> tryAnonymousIp(InetAddress ipAddress) throws IOException,
            GeoIp2Exception {
        return this.get(ipAddress, AnonymousIpResponse.class, DatabaseType.ANONYMOUS_IP, 0);
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
        return this.getOrThrowException(ipAddress, AsnResponse.class, DatabaseType.ASN);
    }

    @Override
    public Optional<AsnResponse> tryAsn(InetAddress ipAddress) throws IOException,
            GeoIp2Exception {
        return this.get(ipAddress, AsnResponse.class, DatabaseType.ASN, 0);
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
        return this.getOrThrowException(ipAddress, ConnectionTypeResponse.class,
                DatabaseType.CONNECTION_TYPE);
    }

    @Override
    public Optional<ConnectionTypeResponse> tryConnectionType(InetAddress ipAddress)
            throws IOException, GeoIp2Exception {
        return this.get(ipAddress, ConnectionTypeResponse.class,
                DatabaseType.CONNECTION_TYPE, 0);
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
        return this
                .getOrThrowException(ipAddress, DomainResponse.class, DatabaseType.DOMAIN);
    }

    @Override
    public Optional<DomainResponse> tryDomain(InetAddress ipAddress) throws IOException,
            GeoIp2Exception {
        return this
                .get(ipAddress, DomainResponse.class, DatabaseType.DOMAIN, 0);
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
        return this.getOrThrowException(ipAddress, EnterpriseResponse.class, DatabaseType.ENTERPRISE);
    }

    @Override
    public Optional<EnterpriseResponse> tryEnterprise(InetAddress ipAddress) throws IOException,
            GeoIp2Exception {
        return this.get(ipAddress, EnterpriseResponse.class, DatabaseType.ENTERPRISE, 0);
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
        return this.getOrThrowException(ipAddress, IspResponse.class, DatabaseType.ISP);
    }

    @Override
    public Optional<IspResponse> tryIsp(InetAddress ipAddress) throws IOException,
            GeoIp2Exception {
        return this.get(ipAddress, IspResponse.class, DatabaseType.ISP, 0);
    }

    /**
     * @return the metadata for the open MaxMind DB file.
     */
    public Metadata getMetadata() {
        return this.reader.getMetadata();
    }
}
