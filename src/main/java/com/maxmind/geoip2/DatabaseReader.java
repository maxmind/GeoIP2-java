package com.maxmind.geoip2;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.maxmind.db.Reader;
import com.maxmind.db.Reader.FileMode;
import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.ConnectionTypeResponse;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.model.DomainResponse;
import com.maxmind.geoip2.model.InsightsResponse;
import com.maxmind.geoip2.model.IspResponse;

/**
 * Instances of this class provide a reader for the GeoIP2 database format. IP
 * addresses can be looked up using the <code>get</code> method.
 */
public class DatabaseReader implements GeoIp2Provider, Closeable {

    private final Reader reader;

    private final ObjectMapper om;

    DatabaseReader(Builder builder) throws IOException {
        if (builder.stream != null) {
            this.reader = new Reader(builder.stream);
        } else if (builder.database != null) {
            this.reader = new Reader(builder.database, builder.mode);
        } else {
            // This should never happen. If it does, review the Builder class
            // constructors for errors.
            throw new IllegalArgumentException(
                    "Unsupported Builder configuration: expected either File or URL");
        }
        this.om = new ObjectMapper();
        this.om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                false);
        this.om.configure(
                DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        InjectableValues inject = new InjectableValues.Std().addValue(
                "locales", builder.locales);
        this.om.setInjectableValues(inject);
    }

    /**
     * Constructs a Builder for the DatabaseReader. The file passed to it must
     * be a valid GeoIP2 database file.
     *
     * <code>Builder</code> creates instances of <code>DatabaseReader</code>
     * from values set by the methods.
     *
     * Only the values set in the <code>Builder</code> constructor are required.
     */
    public final static class Builder {
        final File database;
        final InputStream stream;

        List<String> locales = Arrays.asList("en");
        FileMode mode = FileMode.MEMORY_MAPPED;

        /**
         * @param stream
         *            the stream containing the GeoIP2 database to use.
         */
        public Builder(InputStream stream) {
            this.stream = stream;
            this.database = null;
        }

        /**
         * @param database
         *            the GeoIP2 database file to use.
         */
        public Builder(File database) {
            this.database = database;
            this.stream = null;
        }

        /**
         * @param val
         *            List of locale codes to use in name property from most
         *            preferred to least preferred.
         * @return Builder object
         */
        public Builder locales(List<String> val) {
            this.locales = val;
            return this;
        }

        /**
         * @param val
         *            The file mode used to open the GeoIP2 database
         * @throws java.lang.IllegalArgumentException
         *             if you initialized the Builder with a URL, which uses
         *             {@link FileMode#MEMORY}, but you provided a different
         *             FileMode to this method.
         * @return Builder object
         * */
        public Builder fileMode(FileMode val) {
            if (this.stream != null && !FileMode.MEMORY.equals(val)) {
                throw new IllegalArgumentException(
                        "Only FileMode.MEMORY is supported when using an InputStream.");
            }
            this.mode = val;
            return this;
        }

        /**
         * @return an instance of <code>DatabaseReader</code> created from the
         *         fields set on this builder.
         * @throws IOException
         *             if there is an error reading the database
         */
        public DatabaseReader build() throws IOException {
            return new DatabaseReader(this);
        }
    }

    /**
     * @param ipAddress
     *            IPv4 or IPv6 address to lookup.
     * @return A <T> object with the data for the IP address
     * @throws IOException
     *             if there is an error opening or reading from the file.
     * @throws AddressNotFoundException
     *             if the IP address is not in our database
     */
    private <T> T get(InetAddress ipAddress, Class<T> cls) throws IOException,
            AddressNotFoundException {
        return this.get(ipAddress, cls, true);
    }

    private <T> T get(InetAddress ipAddress, Class<T> cls, boolean hasTraits)
            throws IOException, AddressNotFoundException {
        ObjectNode node = (ObjectNode) this.reader.get(ipAddress);

        // We throw the same exception as the web service when an IP is not in
        // the database
        if (node == null) {
            throw new AddressNotFoundException("The address "
                    + ipAddress.getHostAddress() + " is not in the database.");
        }

        ObjectNode ipNode;
        if (hasTraits) {
            if (!node.has("traits")) {
                node.put("traits", this.om.createObjectNode());
            }
            ipNode = (ObjectNode) node.get("traits");
        } else {
            ipNode = node;
        }
        ipNode.put("ip_address", ipAddress.getHostAddress());

        // The cast and the Omni.class are sort of ugly. There might be a
        // better way
        return this.om.treeToValue(node, cls);
    }

    /**
     * Closes the GeoIP2 database and returns resources to the system.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @Override
    public void close() throws IOException {
        this.reader.close();
    }

    @Override
    public CountryResponse country(InetAddress ipAddress) throws IOException,
            GeoIp2Exception {
        return this.get(ipAddress, CountryResponse.class);
    }

    @Override
    public CityResponse city(InetAddress ipAddress) throws IOException,
            GeoIp2Exception {
        return this.get(ipAddress, CityResponse.class);
    }

    public InsightsResponse insights(InetAddress ipAddress) throws IOException,
    GeoIp2Exception {
            return this.get(ipAddress, InsightsResponse.class);
}

    public ConnectionTypeResponse connectionType(InetAddress ipAddress)
            throws IOException, GeoIp2Exception {
        return this.get(ipAddress, ConnectionTypeResponse.class, false);
    }

    public DomainResponse domain(InetAddress ipAddress) throws IOException,
            GeoIp2Exception {
        return this.get(ipAddress, DomainResponse.class, false);
    }

    public IspResponse isp(InetAddress ipAddress) throws IOException,
            GeoIp2Exception {
        return this.get(ipAddress, IspResponse.class, false);
    }
}
