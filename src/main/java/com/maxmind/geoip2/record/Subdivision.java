package com.maxmind.geoip2.record;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Contains data for the subdivisions associated with an IP address.
 *
 * This record is returned by all the end points except the Country end point.
 */
final public class Subdivision extends AbstractNamedRecord {
    @JsonProperty
    private Integer confidence;

    @JsonProperty("iso_code")
    private String isoCode;

    public Subdivision() {
    }

    /**
     * @return This is a value from 0-100 indicating MaxMind's confidence that
     *         the subdivision is correct. This attribute is only available from
     *         the Omni end point.
     */
    public Integer getConfidence() {
        return this.confidence;
    }

    /**
     * @return This is a string up to three characters long contain the
     *         subdivision portion of the <a
     *         href="http://en.wikipedia.org/wiki/ISO_3166-2">ISO 3166-2code</a>.
     *         This attribute is returned by all end points except Country.
     */
    public String getIsoCode() {
        return this.isoCode;
    }
}
