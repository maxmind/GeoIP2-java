package com.maxmind.geoip2.record;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Contains data for the continent record associated with an IP address.
 * </p>
 * <p>
 * This record is returned by all the end points.
 * </p>
 * <p>
 * Do not use any of the continent names as a database or map key. Use the
 * value returned by {@link #getGeoNameId} or {@link #getCode} instead.
 * </p>
 */
public final class Continent extends AbstractNamedRecord {

    private final String code;

    public Continent() {
        this(null, null, null, null);
    }

    public Continent(
            @JacksonInject("locales") List<String> locales,
            @JsonProperty("code") String code,
            @JsonProperty("geoname_id") Integer geoNameId,
            @JsonProperty("names") Map<String, String> names
    ) {
        super(locales, geoNameId, names);
        this.code = code;
    }

    /**
     * @return A two character continent code like "NA" (North America) or "OC"
     * (Oceania). This attribute is returned by all end points.
     */
    public String getCode() {
        return this.code;
    }

}
