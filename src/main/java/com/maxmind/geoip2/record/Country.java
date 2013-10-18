package com.maxmind.geoip2.record;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Contains data for the country record associated with an IP address.
 *
 * This record is returned by all the end points.
 */
public class Country extends AbstractNamedRecord {
    @JsonProperty
    private Integer confidence;

    @JsonProperty("iso_code")
    private String isoCode;

    public Country() {
        super();
    }

    /**
     * @return A value from 0-100 indicating MaxMind's confidence that the
     *         country is correct. This attribute is only available from the
     *         Omni end point.
     */
    public Integer getConfidence() {
        return this.confidence;
    }

    /**
     * @return The <a
     *         href="http://en.wikipedia.org/wiki/ISO_3166-1">two-character ISO
     *         3166-1 alpha code</a> for the country. This attribute is returned
     *         by all end points.
     */
    public String getIsoCode() {
        return this.isoCode;
    }
}
