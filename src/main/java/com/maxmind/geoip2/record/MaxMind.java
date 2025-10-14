package com.maxmind.geoip2.record;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maxmind.db.MaxMindDbParameter;
import com.maxmind.geoip2.JsonSerializable;

/**
 * <p>
 * Contains data related to your MaxMind account.
 * </p>
 *
 * @param queriesRemaining The number of remaining queries in your account for the current
 *                         web service. This returns {@code null} when called on a database.
 */
public record MaxMind(
    @JsonProperty("queries_remaining")
    @MaxMindDbParameter(name = "queries_remaining")
    Integer queriesRemaining
) implements JsonSerializable {

    /**
     * Constructs a {@code MaxMind} record.
     */
    public MaxMind() {
        this(null);
    }

    /**
     * @return The number of remaining queried in your account for the current
     * web service. This returns {@code null} when called on a database.
     * @deprecated Use {@link #queriesRemaining()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    @JsonProperty("queries_remaining")
    public Integer getQueriesRemaining() {
        return queriesRemaining();
    }
}
