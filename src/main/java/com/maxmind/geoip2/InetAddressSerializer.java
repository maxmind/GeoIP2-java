package com.maxmind.geoip2;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.net.InetAddress;

/**
 * Serializes InetAddress to its host address string representation.
 */
public class InetAddressSerializer extends StdSerializer<InetAddress> {
    /**
     * Constructs an instance of {@code InetAddressSerializer}.
     */
    public InetAddressSerializer() {
        super(InetAddress.class);
    }

    @Override
    public void serialize(InetAddress value, JsonGenerator gen, SerializerProvider provider)
        throws IOException {
        if (value == null) {
            gen.writeNull();
        } else {
            gen.writeString(value.getHostAddress());
        }
    }
}
