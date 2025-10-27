package com.maxmind.geoip2;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;

/**
 * Interface for classes that can be serialized to JSON.
 * Provides default implementation for toJson() method.
 */
public interface JsonSerializable {

    /**
     * @return JSON representation of this object. The structure is the same as
     * the JSON provided by the GeoIP2 web service.
     * @throws IOException if there is an error serializing the object to JSON.
     */
    default String toJson() throws IOException {
        JsonMapper mapper = JsonMapper.builder()
            .disable(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS)
            .addModule(new JavaTimeModule())
            .addModule(new InetAddressModule())
            .serializationInclusion(JsonInclude.Include.NON_NULL)
            .serializationInclusion(JsonInclude.Include.NON_EMPTY)
            .build();

        return mapper.writeValueAsString(this);
    }

}
