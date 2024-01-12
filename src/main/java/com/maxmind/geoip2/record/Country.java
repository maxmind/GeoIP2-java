package com.maxmind.geoip2.record;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.maxmind.db.MaxMindDbConstructor;
import com.maxmind.db.MaxMindDbParameter;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Contains data for the country record associated with an IP address.
 * </p>
 * <p>
 * Do not use any of the country names as a database or map key. Use the value
 * returned by {@link #getGeoNameId} or {@link #getIsoCode} instead.
 * </p>
 */
public class Country extends AbstractNamedRecord {

    private final Integer confidence;
    private final boolean isInEuropeanUnion;
    private final String isoCode;

    public Country() {
        this(null, null, null, false, null, null);
    }

    /** 
     * Constructs an instance of {@code Country}.
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
     */
    @MaxMindDbConstructor
    public Country(
        @JacksonInject("locales") @MaxMindDbParameter(name = "locales") List<String> locales,
        @JsonProperty("confidence") @MaxMindDbParameter(name = "confidence") Integer confidence,
        @JsonProperty("geoname_id") @MaxMindDbParameter(name = "geoname_id") Long geoNameId,
        @JsonProperty("is_in_european_union") @MaxMindDbParameter(name = "is_in_european_union")
        Boolean isInEuropeanUnion,
        @JsonProperty("iso_code") @MaxMindDbParameter(name = "iso_code") String isoCode,
        @JsonProperty("names") @MaxMindDbParameter(name = "names") Map<String, String> names
    ) {
        super(locales, geoNameId, names);
        this.confidence = confidence;
        this.isInEuropeanUnion = isInEuropeanUnion != null ? isInEuropeanUnion : false;
        this.isoCode = isoCode;
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
            country.getConfidence(),
            country.getGeoNameId(),
            country.isInEuropeanUnion(),
            country.getIsoCode(),
            country.getNames()
        );
    }

    /**
     * @return A value from 0-100 indicating MaxMind's confidence that the
     * country is correct. This attribute is only available from the
     * Insights web service and the GeoIP2 Enterprise database.
     */
    public Integer getConfidence() {
        return this.confidence;
    }

    /**
     * @return This is true if the country is a member state of the European
     * Union.
     */
    @JsonProperty("is_in_european_union")
    public boolean isInEuropeanUnion() {
        return this.isInEuropeanUnion;
    }

    /**
     * @return The <a
     * href="https://en.wikipedia.org/wiki/ISO_3166-1">two-character ISO
     * 3166-1 alpha code</a> for the country.
     */
    @JsonProperty("iso_code")
    public String getIsoCode() {
        return this.isoCode;
    }
}
