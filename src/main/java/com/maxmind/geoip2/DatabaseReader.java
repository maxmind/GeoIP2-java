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
import com.maxmind.geoip2.model.CityIspOrgResponse;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.model.OmniResponse;

/**
 * Instances of this class provide a reader for the GeoIP2 database format. IP
 * addresses can be looked up using the <code>get</code> method.
 */
public class DatabaseReader implements GeoIp2Provider, Closeable {

    private final Reader reader;

    private final ObjectMapper om;

    DatabaseReader(Builder builder) throws IOException {
        if (null != builder.stream) {
            this.reader = new Reader(builder.stream);
        } else if (null != builder.database) {
            this.reader = new Reader(builder.database, builder.mode);
        } else {
            throw new IllegalArgumentException(
                    "Unsupported Builder configuration; expected either File or URL");
        }
        this.om = new ObjectMapper();
        this.om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                false);
        InjectableValues inject = new InjectableValues.Std().addValue(
                "locales", builder.locales);
        this.om.setInjectableValues(inject);
    }

    /**
     * Constructs a Builder for the DatabaseReader. The file passed to it must
     * be a valid GeoIP2 database file.
     *
     * <code>Builder</code> creates instances of
     * <code>DatabaseReader</client> from values set by the methods.
     *
     * Only the values set in the <code>Builder</code> constructor are
     * required.
     */
    public final static class Builder {
        final File database;
        final InputStream stream;

        List<String> locales = Arrays.asList("en");
        FileMode mode = FileMode.MEMORY_MAPPED;

        /**
         * @param stream the stream containing the GeoIP2 database to use.
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
         */
        public Builder locales(List<String> val) {
            this.locales = val;
            return this;
        }

        /**
         * @param val
         *            The file mode used to open the GeoIP2 database
         * @throws java.lang.IllegalArgumentException if you initialized
         * the Builder with a URL, which uses {@link FileMode#MEMORY},
         * but you provided a different FileMode to this method.
         * */
        public Builder fileMode(FileMode val) {
            if (null != this.stream && !FileMode.MEMORY.equals(val)) {
                throw new IllegalArgumentException(
                        "I do not support FileMode when using an InputStream");
            }
            this.mode = val;
            return this;
        }

        /**
         * @return an instance of <code>DatabaseReader</code> created from the
         *         fields set on this builder.
         * @throws IOException
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
        ObjectNode node = (ObjectNode) this.reader.get(ipAddress);

        // We throw the same exception as the web service when an IP is not in
        // the database
        if (node == null) {
            throw new AddressNotFoundException("The address "
                    + ipAddress.getHostAddress() + " is not in the database.");
        }

        if (!node.has("traits")) {
            node.put("traits", this.om.createObjectNode());
        }
        ObjectNode traits = (ObjectNode) node.get("traits");
        traits.put("ip_address", ipAddress.getHostAddress());

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

    @Override
    public CityIspOrgResponse cityIspOrg(InetAddress ipAddress)
            throws IOException, GeoIp2Exception {
        return this.get(ipAddress, CityIspOrgResponse.class);
    }

    @Override
    public OmniResponse omni(InetAddress ipAddress) throws IOException,
            GeoIp2Exception {
        return this.get(ipAddress, OmniResponse.class);
    }
}
