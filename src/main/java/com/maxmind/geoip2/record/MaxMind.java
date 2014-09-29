package com.maxmind.geoip2.record;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <p>
 * Contains data related to your MaxMind account.
 * </p>
 * <p>
 * This record is returned by all the end points.
 * </p>
 */
final public class MaxMind {
    @JsonProperty("queries_remaining")
    private Integer queriesRemaining;

    public MaxMind() {
    }

    /**
     * @return The number of remaining queried in your account for the current
     * end point.
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
