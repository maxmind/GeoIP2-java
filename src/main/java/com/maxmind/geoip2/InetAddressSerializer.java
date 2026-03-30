package com.maxmind.geoip2;

import java.net.InetAddress;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ser.std.StdSerializer;

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
    public void serialize(InetAddress value, JsonGenerator gen, SerializationContext provider)
            throws JacksonException {
        if (value == null) {
            gen.writeNull();
        } else {
            gen.writeString(value.getHostAddress());
        }
    }
}
