package com.maxmind.geoip2.record;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <p>
 * Contains data for the continent record associated with an IP address.
 * </p>
 * <p>
 * This record is returned by all the end points.
 * </p>
 */
public final class Continent extends AbstractNamedRecord {
    @JsonProperty("code")
    private String code;

    /**
     * @return A two character continent code like "NA" (North America) or "OC"
     * (Oceania). This attribute is returned by all end points.
     */
    public String getCode() {
        return this.code;
    }
}
