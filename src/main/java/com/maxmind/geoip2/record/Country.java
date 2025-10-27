package com.maxmind.geoip2.record;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.maxmind.db.MaxMindDbParameter;
import com.maxmind.geoip2.NamedRecord;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Contains data for the country record associated with an IP address.
 * </p>
 * <p>
 * Do not use any of the country names as a database or map key. Use the value
 * returned by {@link #geonameId()} or {@link #isoCode()} instead.
 * </p>
 *
 * @param locales The locales to use for retrieving localized names.
 * @param confidence A value from 0-100 indicating MaxMind's confidence that the
 *                   country is correct. This attribute is only available from the
 *                   Insights web service and the GeoIP2 Enterprise database.
 * @param geonameId The GeoName ID for the country.
 * @param isInEuropeanUnion This is true if the country is a member state of the
 *                          European Union.
 * @param isoCode The <a href="https://en.wikipedia.org/wiki/ISO_3166-1">two-character ISO
 *                3166-1 alpha code</a> for the country.
 * @param names A {@link Map} from locale codes to the name in that locale.
 */
public record Country(
    @JacksonInject("locales")
    @MaxMindDbParameter(name = "locales")
    List<String> locales,

    @JsonProperty("confidence")
    @MaxMindDbParameter(name = "confidence")
    Integer confidence,

    @JsonProperty("geoname_id")
    @MaxMindDbParameter(name = "geoname_id")
    Long geonameId,

    @JsonProperty("is_in_european_union")
    @MaxMindDbParameter(name = "is_in_european_union", useDefault = true)
    boolean isInEuropeanUnion,

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
    public Country {
        locales = locales != null ? List.copyOf(locales) : List.of();
        names = names != null ? Map.copyOf(names) : Map.of();
    }

    /**
     * Constructs an instance of {@code Country} with no data.
     */
    public Country() {
        this(null, null, null, false, null, null);
    }

    /**
     * Constructs an instance of {@code Country}.
     *
     * @param country The {@code Country} object to copy.
     * @param locales The locales to use.
     */
    public Country(
        Country country,
        List<String> locales
    ) {
        this(
            locales,
            country.confidence(),
            country.geonameId(),
            country.isInEuropeanUnion(),
            country.isoCode(),
            country.names()
        );
    }

    /**
     * @return A value from 0-100 indicating MaxMind's confidence that the
     * country is correct. This attribute is only available from the
     * Insights web service and the GeoIP2 Enterprise database.
     * @deprecated Use {@link #confidence()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    public Integer getConfidence() {
        return confidence();
    }

    /**
     * @return The <a
     * href="https://en.wikipedia.org/wiki/ISO_3166-1">two-character ISO
     * 3166-1 alpha code</a> for the country.
     * @deprecated Use {@link #isoCode()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    @JsonProperty("iso_code")
    public String getIsoCode() {
        return isoCode();
    }

    /**
     * @return The GeoName ID for the country.
     * @deprecated Use {@link #geonameId()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    @JsonProperty("geoname_id")
    public Long getGeoNameId() {
        return geonameId();
    }

    /**
     * @return The name of the country based on the locales list.
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
