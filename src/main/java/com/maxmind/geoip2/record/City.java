package com.maxmind.geoip2.record;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.maxmind.db.MaxMindDbConstructor;
import com.maxmind.db.MaxMindDbParameter;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * City-level data associated with an IP address.
 * </p>
 * <p>
 * Do not use any of the city names as a database or map key. Use the value
 * returned by {@link #getGeoNameId} instead.
 * </p>
 */
public final class City extends AbstractNamedRecord {

    private final Integer confidence;

    /**
     * Constructs an instance of {@code City} with no data.
     */
    public City() {
        this(null, null, null, null);
    }

    /**
     * Constructs an instance of {@code City} with the specified parameters.
     *
     * @param locales    The locales to use.
     * @param confidence A value from 0-100 indicating MaxMind's confidence that the
     *                   city is correct. .
     * @param geoNameId  The GeoName ID for the city.
     * @param names      A map from locale codes to the city name in that locale.
     */
    @MaxMindDbConstructor
    public City(
        @JacksonInject("locales") @MaxMindDbParameter(name = "locales") List<String> locales,
        @JsonProperty("confidence") @MaxMindDbParameter(name = "confidence") Integer confidence,
        @JsonProperty("geoname_id") @MaxMindDbParameter(name = "geoname_id") Long geoNameId,
        @JsonProperty("names") @MaxMindDbParameter(name = "names") Map<String, String> names
    ) {
        super(locales, geoNameId, names);
        this.confidence = confidence;
    }

    /**
     * Constructs an instance of {@code City}.
     *
     * @param city The {@code City} object to copy.
     * @param locales The locales to use.
     */
    public City(
        City city,
        List<String> locales
    ) {
        this(
            locales,
            city.getConfidence(),
            city.getGeoNameId(),
            city.getNames()
        );
    }

    /**
     * @return A value from 0-100 indicating MaxMind's confidence that the city
     * is correct. This attribute is only available from the Insights
     * web service and the GeoIP2 Enterprise database.
     */
    public Integer getConfidence() {
        return this.confidence;
    }
}
