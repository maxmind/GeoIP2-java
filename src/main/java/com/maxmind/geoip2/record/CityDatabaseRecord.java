package com.maxmind.geoip2.record;

import com.maxmind.db.MaxMindDbConstructor;
import com.maxmind.db.MaxMindDbParameter;

import java.util.Map;

public final class CityDatabaseRecord {
    private final Integer confidence;
    private final Long geoNameId;
    private final Map<String, String> names;

    @MaxMindDbConstructor
    public CityDatabaseRecord(
            @MaxMindDbParameter(name="confidence") Integer confidence,
            @MaxMindDbParameter(name="geoname_id") Long geoNameId,
            @MaxMindDbParameter(name="names") Map<String, String> names
    ) {
        this.confidence = confidence;
        this.geoNameId = geoNameId;
        this.names = names;
    }

    public Integer getConfidence() {
        return this.confidence;
    }

    public Long getGeoNameId() {
        return this.geoNameId;
    }

    public Map<String, String> getNames() {
        return this.names;
    }
}
