package com.maxmind.geoip2;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Deserializes a string to an InetAddress.
 */
public class InetAddressDeserializer extends StdDeserializer<InetAddress> {
    /**
     * Constructs an instance of {@code InetAddressDeserializer}.
     */
    public InetAddressDeserializer() {
        super(InetAddress.class);
    }

    @Override
    public InetAddress deserialize(JsonParser p, DeserializationContext ctxt)
        throws IOException {
        String value = p.getValueAsString();
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return InetAddress.getByName(value);
        } catch (UnknownHostException e) {
            throw new IOException("Invalid IP address: " + value, e);
        }
    }
}
