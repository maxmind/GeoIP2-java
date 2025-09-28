package com.maxmind.geoip2;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.maxmind.db.Network;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * This class provides a deserializer for the Network class.
 */
public final class NetworkDeserializer extends StdDeserializer<Network> {

    /**
     * Constructs a {@code NetworkDeserializer} with no type specified.
     */
    public NetworkDeserializer() {
        this(null);
    }

    /**
     * Constructs a @{code NetworkDeserializer} object.
     *
     * @param vc a class
     */
    public NetworkDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Network deserialize(JsonParser jsonparser, DeserializationContext context)
            throws IOException {

        final String cidr = jsonparser.getValueAsString();
        if (cidr == null || cidr.isBlank()) {
            return null;
        }
        return parseCidr(cidr);
    }

    private static Network parseCidr(String cidr) throws IOException {
        final String[] parts = cidr.split("/", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid CIDR format: " + cidr);
        }

        final String addrPart = parts[0];
        final String prefixPart = parts[1];

        final InetAddress address;
        try {
            address = InetAddress.getByName(addrPart);
        } catch (UnknownHostException e) {
            throw new IOException("Unknown host in CIDR: " + cidr, e);
        }

        final int prefixLength;
        try {
            prefixLength = Integer.parseInt(prefixPart);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Invalid prefix length in CIDR: " + cidr, e);
        }

        final int maxPrefix = (address.getAddress().length == 4) ? 32 : 128;
        if (prefixLength < 0 || prefixLength > maxPrefix) {
            throw new IllegalArgumentException(
                    "Prefix length out of range (0-" + maxPrefix + ") for CIDR: " + cidr);
        }

        return new Network(address, prefixLength);
    }
}
