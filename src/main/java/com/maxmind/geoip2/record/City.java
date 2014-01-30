package com.maxmind.geoip2.record;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * City-level data associated with an IP address.
 *
 * This record is returned by all the end points except the Country end point.
 */
final public class City extends AbstractNamedRecord {
    @JsonProperty
    private Integer confidence;

    public City() {
        super();
    }

    /**
     * @return A value from 0-100 indicating MaxMind's confidence that the city
     *         is correct. This attribute is only available from the Omni end
     *         point.
     */
    public Integer getConfidence() {
        return this.confidence;
    }

}
