package com.maxmind.geoip2.model;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.IOException;

public abstract class AbstractResponse {

    /**
     * @return JSON representation of this object. The structure is the same as
     * the JSON provided by the GeoIP2 web service.
     * @throws IOException if there is an error serializing the object to JSON.
     */
    public String toJson() throws IOException {
        JsonMapper mapper = JsonMapper.builder()
                .disable(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS)
                .serializationInclusion(Include.NON_NULL)
                .serializationInclusion(Include.NON_EMPTY)
                .build();

        return mapper.writeValueAsString(this);
    }

    @Override
    public String toString() {
        // This exception should never happen. If it does happen, we did
        // something wrong.
        try {
            return getClass().getName() + " [ " + toJson() + " ]";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
