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

    public RepresentedCountry() {
        this(null, null, null, false, null, null, null);
    }

    /**
     * Constructs an instance of {@code RepresentedCountry}.
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
