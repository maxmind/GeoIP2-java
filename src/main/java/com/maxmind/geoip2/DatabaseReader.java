package com.maxmind.geoip2;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.maxmind.db.Reader;
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

    /**
     * Constructs a Reader for the GeoIP2 database format. The file passed to it
     * must be a valid GeoIP2 database file.
     * 
     * @param database
     *            the GeoIP2 database file to use.
     * @throws IOException
     *             if there is an error opening or reading from the file.
     */
    public DatabaseReader(File database) throws IOException {
        this(database, Arrays.asList("en"));
    }

    /**
     * Constructs a Reader for the GeoIP2 database format. The file passed to it
     * must be a valid GeoIP2 database file.
     * 
     * @param database
     *            the GeoIP2 database file to use.
     * @param languages
     *            List of language codes to use in name property from most
     *            preferred to least preferred.
     * @throws IOException
     *             if there is an error opening or reading from the file.
     */
    public DatabaseReader(File database, List<String> languages)
            throws IOException {
        this.reader = new Reader(database);
        this.om = new ObjectMapper();
        this.om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                false);
        InjectableValues inject = new InjectableValues.Std().addValue(
                "languages", languages);
        this.om.setInjectableValues(inject);
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
