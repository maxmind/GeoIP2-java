package com.maxmind.geoip2.record;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.maxmind.db.MaxMindDbConstructor;
import com.maxmind.db.MaxMindDbParameter;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Contains data for the represented country associated with an IP address.
 * </p>
 * <p>
 * This class contains the country-level data associated with an IP address for
 * the IP's represented country. The represented country is the country
 * represented by something like a military base.
 * </p>
 * <p>
 * Do not use any of the country names as a database or map key. Use the value
 * returned by {@link #getGeoNameId} or {@link #getIsoCode} instead.
 * </p>
 */
public final class RepresentedCountry extends Country {

    private final String type;

    /**
     * Constructs an instance of {@code RepresentedCountry} with no data.
     */
    public RepresentedCountry() {
        this(null, null, null, false, null, null, null);
    }

    /**
     * Constructs an instance of {@code RepresentedCountry}.
     *
     * @param locales The locales to use.
     * @param confidence This is a value from 0-100 indicating MaxMind's
     * confidence that the country is correct.
     * @param geoNameId This is a GeoName ID for the country.
     * @param isInEuropeanUnion This is true if the country is a member state of
     * the European Union.
     * @param isoCode This is a string up to three characters long contain the
     * country code.
     * @param names This is a map from locale codes to the names for the country
     * in that locale.
     * @param type This is a string indicating the type of entity that is
     * representing the country.
     */
    @MaxMindDbConstructor
    public RepresentedCountry(
        @JacksonInject("locales") @MaxMindDbParameter(name = "locales") List<String> locales,
        @JsonProperty("confidence") @MaxMindDbParameter(name = "confidence") Integer confidence,
        @JsonProperty("geoname_id") @MaxMindDbParameter(name = "geoname_id") Long geoNameId,
        @JsonProperty("is_in_european_union") @MaxMindDbParameter(name = "is_in_european_union")
        Boolean isInEuropeanUnion,
        @JsonProperty("iso_code") @MaxMindDbParameter(name = "iso_code") String isoCode,
        @JsonProperty("names") @MaxMindDbParameter(name = "names") Map<String, String> names,
        @JsonProperty("type") @MaxMindDbParameter(name = "type") String type
    ) {
        super(locales, confidence, geoNameId, isInEuropeanUnion, isoCode,
            names);
        this.type = type;
    }

    /** 
     * Constructs an instance of {@code RepresentedCountry}.
     *
     * @param country The {@code RepresentedCountry} object to copy.
     * @param locales The locales to use.
     */
    public RepresentedCountry(
        RepresentedCountry country,
        List<String> locales
    ) {
        this(
            locales,
            country.getConfidence(),
            country.getGeoNameId(),
            country.isInEuropeanUnion(),
            country.getIsoCode(),
            country.getNames(),
            country.getType()
        );
    }

    /**
     * @return A string indicating the type of entity that is representing the
     * country. Currently, we only return {@code military} but this could
     * expand to include other types in the future.
     */
    public String getType() {
        return this.type;
    }

}
