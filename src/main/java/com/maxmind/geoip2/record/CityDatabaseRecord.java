package com.maxmind.geoip2.record;

import com.maxmind.db.MaxMindDbConstructor;
import com.maxmind.db.MaxMindDbParameter;

import java.util.Map;

public final class CityDatabaseRecord {
    private final Long geoNameId;
    private final Map<String, String> names;

    @MaxMindDbConstructor
    public CityDatabaseRecord(
            @MaxMindDbParameter(name="geoname_id") Long geoNameId,
            @MaxMindDbParameter(name="names") Map<String, String> names
    ) {
        this.geoNameId = geoNameId;
        this.names = names;
    }

    public Long getGeoNameId() {
        return this.geoNameId;
    }

    public Map<String, String> getNames() {
        return this.names;
    }
}
