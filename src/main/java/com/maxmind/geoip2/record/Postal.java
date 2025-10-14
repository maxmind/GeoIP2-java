package com.maxmind.geoip2.record;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maxmind.db.MaxMindDbParameter;
import com.maxmind.geoip2.JsonSerializable;

/**
 * <p>
 * Contains data for the postal record associated with an IP address.
 * </p>
 *
 * @param code The postal code of the location. Postal codes are not available for all
 *             countries. In some countries, this will only contain part of the postal
 *             code.
 * @param confidence A value from 0-100 indicating MaxMind's confidence that the postal
 *                   code is correct. This attribute is only available from the Insights
 *                   web service and the GeoIP2 Enterprise database.
 */
public record Postal(
    @JsonProperty("code")
    @MaxMindDbParameter(name = "code")
    String code,

    @JsonProperty("confidence")
    @MaxMindDbParameter(name = "confidence")
    Integer confidence
) implements JsonSerializable {

    /**
     * Constructs a {@code Postal} record.
     */
    public Postal() {
        this(null, null);
    }

    /**
     * @return The postal code of the location. Postal codes are not available
     * for all countries. In some countries, this will only contain part
     * of the postal code.
     * @deprecated Use {@link #code()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    public String getCode() {
        return code();
    }

    /**
     * @return A value from 0-100 indicating MaxMind's confidence that the
     * postal code is correct. This attribute is only available from the
     * Insights web service and the GeoIP2 Enterprise database.
     * @deprecated Use {@link #confidence()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    public Integer getConfidence() {
        return confidence();
    }
}
