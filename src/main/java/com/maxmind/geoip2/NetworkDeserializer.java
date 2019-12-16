package com.maxmind.geoip2;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.maxmind.db.Network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetworkDeserializer extends StdDeserializer<Network> {

    public NetworkDeserializer() {
        this(null);
    }

    public NetworkDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Network deserialize(
            JsonParser jsonparser, DeserializationContext context)
            throws IOException {

        String cidr = jsonparser.getText();
        if (cidr == null) {
            return null;
        }
        String[] parts = cidr.split("/", 2);
        if (parts.length != 2) {
            throw new RuntimeException("Invalid cidr format: " + cidr);
        }
        int prefixLength = Integer.parseInt(parts[1]);
        try {
            return new Network(InetAddress.getByName(parts[0]), prefixLength);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}