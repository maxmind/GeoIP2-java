package com.maxmind.geoip2.record;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.maxmind.db.MaxMindDbParameter;
import com.maxmind.geoip2.NamedRecord;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * City-level data associated with an IP address.
 * </p>
 * <p>
 * Do not use any of the city names as a database or map key. Use the value
 * returned by {@link #geonameId()} instead.
 * </p>
 *
 * @param locales The locales to use for retrieving localized names.
 * @param confidence A value from 0-100 indicating MaxMind's confidence that the city
 *                   is correct. This attribute is only available from the Insights
 *                   web service and the GeoIP2 Enterprise database.
 * @param geonameId The GeoName ID for the city.
 * @param names A {@link Map} from locale codes to the name in that locale.
 */
public record City(
    @JacksonInject("locales")
    @MaxMindDbParameter(name = "locales")
    List<String> locales,

    @JsonProperty("confidence")
    @MaxMindDbParameter(name = "confidence")
    Integer confidence,

    @JsonProperty("geoname_id")
    @MaxMindDbParameter(name = "geoname_id")
    Long geonameId,

    @JsonProperty("names")
    @MaxMindDbParameter(name = "names")
    Map<String, String> names
) implements NamedRecord {

    /**
     * Compact canonical constructor that ensures immutability and handles null values.
     */
    public City {
        locales = locales != null ? List.copyOf(locales) : List.of();
        names = names != null ? Map.copyOf(names) : Map.of();
    }

    /**
     * Constructs an instance of {@code City} with no data.
     */
    public City() {
        this(null, null, null, null);
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
            city.confidence(),
            city.geonameId(),
            city.names()
        );
    }

    /**
     * @return A value from 0-100 indicating MaxMind's confidence that the city
     * is correct. This attribute is only available from the Insights
     * web service and the GeoIP2 Enterprise database.
     * @deprecated Use {@link #confidence()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    public Integer getConfidence() {
        return confidence();
    }

    /**
     * @return The GeoName ID for the city.
     * @deprecated Use {@link #geonameId()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    @JsonProperty("geoname_id")
    public Long getGeoNameId() {
        return geonameId();
    }

    /**
     * @return The name of the city based on the locales list passed to the
     * constructor.
     * @deprecated Use {@link #name()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    @com.fasterxml.jackson.annotation.JsonIgnore
    public String getName() {
        return name();
    }

    /**
     * @return A {@link Map} from locale codes to the name in that locale.
     * @deprecated Use {@link #names()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    @JsonProperty("names")
    public Map<String, String> getNames() {
        return names();
    }
}
