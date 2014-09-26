package com.maxmind.geoip2.model;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AbstractResponse {

    /**
     * @return JSON representation of this object. The structure is the same as
     * the JSON provided by the GeoIP2 web service.
     * @throws IOException if there is an error serializing the object to JSON.
     */
    public String toJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(Include.NON_NULL);
        mapper.setSerializationInclusion(Include.NON_EMPTY);
        return mapper.writeValueAsString(this);
    }

}
