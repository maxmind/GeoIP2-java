package com.maxmind.geoip2.record;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Contains data related to your MaxMind account.
 *
 * This record is returned by all the end points.
 */
final public class MaxMind implements Serializable {
    private static final long serialVersionUID = -7827300875468506591L;

    @JsonProperty("queries_remaining")
    private Integer queriesRemaining;

    public MaxMind() {
    }

    /**
     * @return The number of remaining queried in your account for the current
     *         end point.
     */
    public Integer getQueriesRemaining() {
        return this.queriesRemaining;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "MaxMind ["
                + (this.getQueriesRemaining() != null ? "getQueriesRemaining()="
                        + this.getQueriesRemaining()
                        : "") + "]";
    }
}
