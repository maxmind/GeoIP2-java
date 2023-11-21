package com.maxmind.geoip2.record;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maxmind.db.MaxMindDbConstructor;
import com.maxmind.db.MaxMindDbParameter;

/**
 * <p>
 * Contains data related to your MaxMind account.
 * </p>
 */
public final class MaxMind extends AbstractRecord {

    private final Integer queriesRemaining;

    public MaxMind() {
        this(null);
    }

    @MaxMindDbConstructor
    public MaxMind(@JsonProperty("queries_remaining") @MaxMindDbParameter(name="queries_remaining") Integer queriesRemaining) {
        this.queriesRemaining = queriesRemaining;
    }

    /**
     * @return The number of remaining queried in your account for the current
     * web service. This returns {@code null} when called on a database.
     */
    @JsonProperty("queries_remaining")
    public Integer getQueriesRemaining() {
        return this.queriesRemaining;
    }
}
