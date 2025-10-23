package com.maxmind.geoip2;

import com.fasterxml.jackson.databind.module.SimpleModule;
import java.net.InetAddress;

/**
 * Jackson module for InetAddress serialization and deserialization.
 */
public class InetAddressModule extends SimpleModule {
    /**
     * Constructs an instance of {@code InetAddressModule}.
     */
    public InetAddressModule() {
        super("InetAddressModule");
        addSerializer(InetAddress.class, new InetAddressSerializer());
        addDeserializer(InetAddress.class, new InetAddressDeserializer());
    }
}
