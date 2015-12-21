package com.maxmind.geoip2.record;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public abstract class AbstractRecord {

    /**
     * @return JSON representation of this object. The structure is the same as
     * the JSON provided by the GeoIP2 web service.
     * @throws IOException if there is an error serializing the object to JSON.
     */
    public String toJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.configure(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS, false);
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
