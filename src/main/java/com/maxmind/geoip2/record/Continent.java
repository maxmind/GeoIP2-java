package com.maxmind.geoip2.record;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.maxmind.db.MaxMindDbConstructor;
import com.maxmind.db.MaxMindDbParameter;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Contains data for the continent record associated with an IP address.
 * </p>
 * <p>
 * This record is returned by all the end points.
 * </p>
 * <p>
 * Do not use any of the continent names as a database or map key. Use the
 * value returned by {@link #getGeoNameId} or {@link #getCode} instead.
 * </p>
 */
public final class Continent extends AbstractNamedRecord {

    private final String code;

    public Continent() {
        this(null, null, (Integer) null, null);
    }

    public Continent(
            @JacksonInject("locales") List<String> locales,
            @JsonProperty("code") String code,
            @JsonProperty("geoname_id") Integer geoNameId,
            @JsonProperty("names") Map<String, String> names
    ) {
        super(locales, geoNameId, names);
        this.code = code;
    }

    @MaxMindDbConstructor
    public Continent(
            @MaxMindDbParameter(name = "locales") List<String> locales,
            @MaxMindDbParameter(name = "code") String code,
            @MaxMindDbParameter(name = "geoname_id") Long geoNameId,
            @MaxMindDbParameter(name = "names") Map<String, String> names
    ) {
        this(
                locales,
                code,
                geoNameId != null ? geoNameId.intValue() : null,
                names
        );
    }

    public Continent(
            Continent continent,
            List<String> locales
    ) {
        this(
                locales,
                continent.getCode(),
                continent.getGeoNameId(),
                continent.getNames()
        );
    }

    /**
     * @return A two character continent code like "NA" (North America) or "OC"
     * (Oceania). This attribute is returned by all end points.
     */
    public String getCode() {
        return this.code;
    }

}
