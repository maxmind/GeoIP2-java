package com.maxmind.geoip2.record;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.maxmind.db.MaxMindDbConstructor;
import com.maxmind.db.MaxMindDbParameter;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Contains data for the subdivisions associated with an IP address.
 * </p>
 * <p>
 * Do not use any of the subdivision names as a database or map key. Use the
 * value returned by {@link #getGeoNameId} or {@link #getIsoCode} instead.
 * </p>
 */
public final class Subdivision extends AbstractNamedRecord {

    private final Integer confidence;
    private final String isoCode;

    public Subdivision() {
        this(null, null, null, null, null);
    }

   /**
    * Constructs an instance of {@code Subdivision}.
    */
    @MaxMindDbConstructor
    public Subdivision(
        @JacksonInject("locales") @MaxMindDbParameter(name = "locales") List<String> locales,
        @JsonProperty("confidence") @MaxMindDbParameter(name = "confidence") Integer confidence,
        @JsonProperty("geoname_id") @MaxMindDbParameter(name = "geoname_id") Long geoNameId,
        @JsonProperty("iso_code") @MaxMindDbParameter(name = "iso_code") String isoCode,
        @JsonProperty("names") @MaxMindDbParameter(name = "names") Map<String, String> names
    ) {
        super(locales, geoNameId, names);
        this.confidence = confidence;
        this.isoCode = isoCode;
    }

    /**
     * Constructs an instance of {@code Subdivision}.
     */
    public Subdivision(
        Subdivision subdivision,
        List<String> locales
    ) {
        this(
            locales,
            subdivision.getConfidence(),
            subdivision.getGeoNameId(),
            subdivision.getIsoCode(),
            subdivision.getNames()
        );
    }

    /**
     * @return This is a value from 0-100 indicating MaxMind's confidence that
     * the subdivision is correct. This attribute is only available from
     * the Insights web service and the GeoIP2 Enterprise database.
     */
    @JsonProperty("confidence")
    public Integer getConfidence() {
        return this.confidence;
    }

    /**
     * @return This is a string up to three characters long contain the
     * subdivision portion of the <a
     * href="https://en.wikipedia.org/wiki/ISO_3166-2">ISO
     * 3166-2code</a>.
     */
    @JsonProperty("iso_code")
    public String getIsoCode() {
        return this.isoCode;
    }
}
