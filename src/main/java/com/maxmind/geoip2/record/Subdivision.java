package com.maxmind.geoip2.record;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.maxmind.db.MaxMindDbParameter;
import com.maxmind.geoip2.NamedRecord;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Contains data for the subdivisions associated with an IP address.
 * </p>
 * <p>
 * Do not use any of the subdivision names as a database or map key. Use the
 * value returned by {@link #geonameId()} or {@link #isoCode()} instead.
 * </p>
 *
 * @param locales The locales to use for retrieving localized names.
 * @param confidence A value from 0-100 indicating MaxMind's confidence that the
 *                   subdivision is correct. This attribute is only available from
 *                   the Insights web service and the GeoIP2 Enterprise database.
 * @param geonameId The GeoName ID for the subdivision.
 * @param isoCode A string up to three characters long containing the subdivision
 *                portion of the <a href="https://en.wikipedia.org/wiki/ISO_3166-2">ISO
 *                3166-2 code</a>.
 * @param names A {@link Map} from locale codes to the name in that locale.
 */
public record Subdivision(
    @JacksonInject("locales")
    @MaxMindDbParameter(name = "locales")
    List<String> locales,

    @JsonProperty("confidence")
    @MaxMindDbParameter(name = "confidence")
    Integer confidence,

    @JsonProperty("geoname_id")
    @MaxMindDbParameter(name = "geoname_id")
    Long geonameId,

    @JsonProperty("iso_code")
    @MaxMindDbParameter(name = "iso_code")
    String isoCode,

    @JsonProperty("names")
    @MaxMindDbParameter(name = "names")
    Map<String, String> names
) implements NamedRecord {

    /**
     * Compact canonical constructor that ensures immutability and handles null values.
     */
    public Subdivision {
        locales = locales != null ? List.copyOf(locales) : List.of();
        names = names != null ? Map.copyOf(names) : Map.of();
    }

    /**
     * Constructs a {@code Subdivision} record.
     */
    public Subdivision() {
        this(null, null, null, null, null);
    }

    /**
     * Constructs an instance of {@code Subdivision} with the specified parameters.
     *
     * @param subdivision The {@code Subdivision} object to copy.
     * @param locales The locales to use.
     */
    public Subdivision(
        Subdivision subdivision,
        List<String> locales
    ) {
        this(
            locales,
            subdivision.confidence(),
            subdivision.geonameId(),
            subdivision.isoCode(),
            subdivision.names()
        );
    }

    /**
     * @return This is a value from 0-100 indicating MaxMind's confidence that
     * the subdivision is correct. This attribute is only available from
     * the Insights web service and the GeoIP2 Enterprise database.
     * @deprecated Use {@link #confidence()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    @JsonProperty("confidence")
    public Integer getConfidence() {
        return confidence();
    }

    /**
     * @return This is a string up to three characters long contain the
     * subdivision portion of the <a
     * href="https://en.wikipedia.org/wiki/ISO_3166-2">ISO
     * 3166-2code</a>.
     * @deprecated Use {@link #isoCode()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    @JsonProperty("iso_code")
    public String getIsoCode() {
        return isoCode();
    }

    /**
     * @return The GeoName ID for the subdivision.
     * @deprecated Use {@link #geonameId()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    @JsonProperty("geoname_id")
    public Long getGeoNameId() {
        return geonameId();
    }

    /**
     * @return The name of the subdivision based on the locales list.
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
