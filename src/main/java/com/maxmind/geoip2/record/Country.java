package com.maxmind.geoip2.record;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Contains data for the country record associated with an IP address.
 * </p>
 * <p>
 * This record is returned by all the end points.
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

    // This method is for backwards compatibility. We should remove it when we
    // do a major release.
    public Country(
            List<String> locales,
            Integer confidence,
            Integer geoNameId,
            String isoCode,
            Map<String, String> names
    ) {
        this(locales, confidence, geoNameId, false, isoCode, names);
    }

    public Country(
            @JacksonInject("locales") List<String> locales,
            @JsonProperty("confidence") Integer confidence,
            @JsonProperty("geoname_id") Integer geoNameId,
            @JsonProperty("is_in_european_union") boolean isInEuropeanUnion,
            @JsonProperty("iso_code") String isoCode,
            @JsonProperty("names") Map<String, String> names
    ) {
        super(locales, geoNameId, names);
        this.confidence = confidence;
        this.isInEuropeanUnion = isInEuropeanUnion;
        this.isoCode = isoCode;
    }

    /**
     * @return A value from 0-100 indicating MaxMind's confidence that the
     * country is correct. This attribute is only available from the
     * Insights end point and the GeoIP2 Enterprise database.
     */
    public Integer getConfidence() {
        return this.confidence;
    }

    /**
     * @return This is true if the country is a member state of the European
     * Union. This attribute is returned by all location services and
     * databases.
     */
    @JsonProperty("is_in_european_union")
    public boolean isInEuropeanUnion() {
        return this.isInEuropeanUnion;
    }

    /**
     * @return The <a
     * href="http://en.wikipedia.org/wiki/ISO_3166-1">two-character ISO
     * 3166-1 alpha code</a> for the country. This attribute is returned
     * by all end points.
     */
    @JsonProperty("iso_code")
    public String getIsoCode() {
        return this.isoCode;
    }

}
