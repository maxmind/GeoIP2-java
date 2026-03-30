package com.maxmind.geoip2;

import com.fasterxml.jackson.annotation.JsonInclude;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.json.JsonMapper;

/**
 * Interface for classes that can be serialized to JSON.
 * Provides default implementation for toJson() method.
 */
public interface JsonSerializable {

    /**
     * @return JSON representation of this object. The structure is the same as
     * the JSON provided by the GeoIP2 web service.
     */
    default String toJson() {
        JsonMapper mapper = JsonMapper.builder()
            .disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
            .addModule(new InetAddressModule())
            .changeDefaultPropertyInclusion(
                        value -> value.withValueInclusion(JsonInclude.Include.NON_NULL)
                                .withValueInclusion(JsonInclude.Include.NON_EMPTY))
            .build();

        return mapper.writeValueAsString(this);
    }

}
