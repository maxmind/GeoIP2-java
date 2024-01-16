package com.maxmind.geoip2.record;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maxmind.db.MaxMindDbConstructor;
import com.maxmind.db.MaxMindDbParameter;

/**
 * <p>
 * Contains data for the postal record associated with an IP address.
 * </p>
 */
public final class Postal extends AbstractRecord {

    private final String code;
    private final Integer confidence;

    /**
     * Constructs a {@code Postal} record.
     */
    public Postal() {
        this(null, null);
    }

    /**
     * Constructs an instance of {@code Postal}.
     *
     * @param code       The postal code of the location. Postal codes are not available
     *                   for all countries. In some countries, this will only contain part
     *                   of the postal code.
     * @param confidence A value from 0-100 indicating MaxMind's confidence that the
     *                   postal code is correct. This attribute is only available from the
     *                   Insights web service and the GeoIP2 Enterprise database.
     */
    @MaxMindDbConstructor
    public Postal(
        @JsonProperty("code") @MaxMindDbParameter(name = "code") String code,
        @JsonProperty("confidence") @MaxMindDbParameter(name = "confidence") Integer confidence
    ) {
        this.code = code;
        this.confidence = confidence;
    }

    /**
     * @return The postal code of the location. Postal codes are not available
     * for all countries. In some countries, this will only contain part
     * of the postal code.
     */
    public String getCode() {
        return this.code;
    }

    /**
     * @return A value from 0-100 indicating MaxMind's confidence that the
     * postal code is correct. This attribute is only available from the
     * Insights web service and the GeoIP2 Enterprise database.
     */
    public Integer getConfidence() {
        return this.confidence;
    }
}
