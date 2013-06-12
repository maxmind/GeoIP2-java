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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maxmind.geoip2.model.CountryLookup;
import com.maxmind.geoip2.model.OmniLookup;

/**
 *
 */
public class Reader implements Closeable {

    // This is sort of annoying. Rename one of the two?
    private final com.maxmind.maxminddb.Reader reader;

    private final ObjectMapper om;

    private final List<String> languages;

    /**
     * 
     */
    public Reader(File database) throws IOException {
        this(database, Arrays.asList("en"));
    }

    public Reader(File database, List<String> languages) throws IOException {
        this.languages = languages;
        this.reader = new com.maxmind.maxminddb.Reader(database);
        this.om = new ObjectMapper();
        InjectableValues inject = new InjectableValues.Std().addValue(
                "languages", this.languages);
        this.om.setInjectableValues(inject);
    }

    public <T extends CountryLookup> T get(InetAddress ipAddress)
            throws IOException {
        JsonNode rawLookup = this.reader.get(ipAddress);
        // XXX - Maybe we should return an empty object so that clients don't
        // have
        // to worry about null pointers. If we do this, it would be nice to add
        // a field
        // to traits or something that clients could check if they had a need
        // (e.g., isMissingFromDatabase).
        if (rawLookup == null) {
            return null;
        }
        // The cast is sort of ugly. There might be a better way
        T lookup = (T) this.om.treeToValue(rawLookup, OmniLookup.class);
        return lookup;
    }

    @Override
    public void close() throws IOException {
        this.reader.close();
    }
}
