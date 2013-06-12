/**
 * 
 */
package com.maxmind.geoip2.database;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.maxmind.geoip2.model.CountryLookup;
import com.maxmind.geoip2.model.OmniLookup;

/**
 *
 */
public class Reader implements Closeable {

    // This is sort of annoying. Rename one of the two?
    private final com.maxmind.maxminddb.Reader reader;

    private final ObjectMapper om;

    /**
     * 
     */
    public Reader(File database) throws IOException {
        this(database, Arrays.asList("en"));
    }

    public Reader(File database, List<String> languages) throws IOException {
        this.reader = new com.maxmind.maxminddb.Reader(database);
        this.om = new ObjectMapper();
        InjectableValues inject = new InjectableValues.Std().addValue(
                "languages", languages);
        this.om.setInjectableValues(inject);
    }

    public <T extends CountryLookup> T get(InetAddress ipAddress)
            throws IOException, AddressNotFoundException {
        ObjectNode node = (ObjectNode) this.reader.get(ipAddress);

        // XXX - I am not sure Java programmers would expect an exception here,
        // but the web service code does throw an exception in this case. If we
        // keep this exception, we should adjust the web service to throw the
        // same exception when it gets and IP_ADDRESS_NOT_FOUND error.
        if (node == null) {
            throw new AddressNotFoundException("The address "
                    + ipAddress.getHostAddress() + " is not in the database.");
        }

        if (!node.has("traits")) {
            node.put("traits", this.om.createObjectNode());
        }
        ObjectNode traits = (ObjectNode) node.get("traits");
        traits.put("ip_address", ipAddress.getHostAddress());

        // The cast and the OmniLookup.class are sort of ugly. There might be a
        // better way
        return (T) this.om.treeToValue(node, OmniLookup.class);
    }

    @Override
    public void close() throws IOException {
        this.reader.close();
    }
}
