package com.maxmind.geoip2.record;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * City-level data associated with an IP address.
 * </p>
 * <p>
 * This record is returned by all the end points except the Country end point.
 * </p>
 * <p>
 * Do not use any of the city names as a database or map key. Use the value
 * returned by {@link #getGeoNameId} instead.
 * </p>
 */
public final class City extends AbstractNamedRecord {

    private final Integer confidence;

    public City() {
        this(null, null, null, null);
    }

    public City(
            @JacksonInject("locales") List<String> locales,
            @JsonProperty("confidence") Integer confidence,
            @JsonProperty("geoname_id") Integer geoNameId,
            @JsonProperty("names") Map<String, String> names
    ) {
        super(locales, geoNameId, names);
        this.confidence = confidence;
    }

    /**
     * @return A value from 0-100 indicating MaxMind's confidence that the city
     * is correct. This attribute is only available from the Insights
     * end point and the GeoIP2 Enterprise database.
     */
    public Integer getConfidence() {
        return this.confidence;
    }
}
