package com.maxmind.geoip2.record;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * Contains data for the continent record associated with an IP address.
 * </p>
 * <p>
 * This record is returned by all the end points.
 * </p>
 */
public final class Continent extends AbstractNamedRecord {

    private final String code;

    public Continent(@JsonProperty("names") HashMap<String, String> names, @JsonProperty("code") String code,
                     @JsonProperty("geoname_id") Integer geoNameId, @JacksonInject("locales") List<String> locales) {
        super(names, geoNameId, locales);
        this.code = code;
    }

    /**
     * @return A two character continent code like "NA" (North America) or "OC"
     * (Oceania). This attribute is returned by all end points.
     */
    public String getCode() {
        return this.code;
    }

    public static Continent empty() {
        return new Continent(null, null, null, null);
    }

}
