package com.maxmind.geoip2.record;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.maxmind.db.MaxMindDbParameter;
import com.maxmind.geoip2.NamedRecord;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Contains data for the continent record associated with an IP address.
 * </p>
 * <p>
 * Do not use any of the continent names as a database or map key. Use the
 * value returned by {@link #geonameId()} or {@link #code()} instead.
 * </p>
 *
 * @param locales The locales to use for retrieving localized names.
 * @param code A two character continent code like "NA" (North America) or "OC"
 *             (Oceania).
 * @param geonameId The GeoName ID for the continent.
 * @param names A {@link Map} from locale codes to the name in that locale.
 */
public record Continent(
    @JacksonInject("locales")
    @MaxMindDbParameter(name = "locales")
    List<String> locales,

    @JsonProperty("code")
    @MaxMindDbParameter(name = "code")
    String code,

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
    public Continent {
        locales = locales != null ? List.copyOf(locales) : List.of();
        names = names != null ? Map.copyOf(names) : Map.of();
    }

    /**
     * Constructs an instance of {@code Continent} with no data.
     */
    public Continent() {
        this(null, null, null, null);
    }

    /**
     * Constructs an instance of {@code Continent}.
     *
     * @param continent The {@code Continent} object to copy.
     * @param locales The locales to use.
     */
    public Continent(
        Continent continent,
        List<String> locales
    ) {
        this(
            locales,
            continent.code(),
            continent.geonameId(),
            continent.names()
        );
    }

    /**
     * @return A two character continent code like "NA" (North America) or "OC"
     * (Oceania).
     * @deprecated Use {@link #code()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    public String getCode() {
        return code();
    }

    /**
     * @return The GeoName ID for the continent.
     * @deprecated Use {@link #geonameId()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    @JsonProperty("geoname_id")
    public Long getGeoNameId() {
        return geonameId();
    }

    /**
     * @return The name of the continent based on the locales list.
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
