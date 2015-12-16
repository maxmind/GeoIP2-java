package com.maxmind.geoip2.record;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;

/**
 * <p>
 * Contains data for the subdivisions associated with an IP address.
 * </p>
 * <p>
 * This record is returned by all the end points except the Country end point.
 * </p>
 */
public final class Subdivision extends AbstractNamedRecord {

    private final Integer confidence;
    private final String isoCode;

    public Subdivision(@JsonProperty("confidence") Integer confidence, @JsonProperty("iso_code") String isoCode,
                       @JsonProperty("geoname_id") Integer geoNameId, @JsonProperty("names") Map<String, String> names,
                       @JacksonInject("locales") List<String> locales) {
        super(names, geoNameId, locales);
        this.confidence = confidence;
        this.isoCode = isoCode;
    }

    /**
     * @return This is a value from 0-100 indicating MaxMind's confidence that
     * the subdivision is correct. This attribute is only available from
     * the Insights end point.
     */
    @JsonProperty("confidence")
    public Integer getConfidence() {
        return this.confidence;
    }

    /**
     * @return This is a string up to three characters long contain the
     * subdivision portion of the <a
     * href="http://en.wikipedia.org/wiki/ISO_3166-2">ISO
     * 3166-2code</a>. This attribute is returned by all end points
     * except Country.
     */
    @JsonProperty("iso_code")
    public String getIsoCode() {
        return this.isoCode;
    }


    public static Subdivision empty() {
        return new Subdivision(null, null, null, new HashMap<String, String>(), new ArrayList<String>());
    }
}
