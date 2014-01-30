package com.maxmind.geoip2.record;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Contains data for the represented country associated with an IP address.
 *
 * This class contains the country-level data associated with an IP address for
 * the IP's represented country. The represented country is the country
 * represented by something like a military base or embassy.
 *
 * This record is returned by all the end points.
 */
final public class RepresentedCountry extends Country {
    @JsonProperty
    private String type;

    public RepresentedCountry() {
    }

    /**
     * @return A string indicating the type of entity that is representing the
     *         country. Currently we only return {@code military} but this could
     *         expand to include other types such as {@code embassy} in the
     *         future. Returned by all end points.
     */
    public String getType() {
        return this.type;
    }
}
