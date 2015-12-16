package com.maxmind.geoip2.record;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * City-level data associated with an IP address.
 * </p>
 * <p>
 * This record is returned by all the end points except the Country end point.
 * </p>
 */
public final class City extends AbstractNamedRecord {

    private final Integer confidence;

    public City(@JsonProperty("names") HashMap<String, String> names, @JsonProperty("geoname_id") Integer geoNameId,
                @JacksonInject("locales") List<String> locales, @JsonProperty("confidence") Integer confidence) {
        super(names, geoNameId, locales);
        this.confidence = confidence;
    }

    /**
     * @return A value from 0-100 indicating MaxMind's confidence that the city
     * is correct. This attribute is only available from the Insights
     * end point.
     */
    public Integer getConfidence() {
        return this.confidence;
    }

    public static City empty() {
        return new City(null, null, null, null);
    }

}
