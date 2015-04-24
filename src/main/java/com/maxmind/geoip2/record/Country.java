package com.maxmind.geoip2.record;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <p>
 * Contains data for the country record associated with an IP address.
 * </p>
 * <p>
 * This record is returned by all the end points.
 * </p>
 */
public class Country extends AbstractNamedRecord {
    @JsonProperty
    private Integer confidence;

    @JsonProperty("iso_code")
    private String isoCode;

    /**
     * @return A value from 0-100 indicating MaxMind's confidence that the
     * country is correct. This attribute is only available from the
     * Insights end point.
     */
    public Integer getConfidence() {
        return this.confidence;
    }

    /**
     * @return The <a
     * href="http://en.wikipedia.org/wiki/ISO_3166-1">two-character ISO
     * 3166-1 alpha code</a> for the country. This attribute is returned
     * by all end points.
     */
    public String getIsoCode() {
        return this.isoCode;
    }
}
