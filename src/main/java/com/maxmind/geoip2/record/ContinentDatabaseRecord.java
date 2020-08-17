package com.maxmind.geoip2.record;

import com.maxmind.db.MaxMindDbConstructor;
import com.maxmind.db.MaxMindDbParameter;

import java.util.Map;

public final class ContinentDatabaseRecord {
    private final String code;
    private final Long geoNameId;
    private final Map<String, String> names;

    @MaxMindDbConstructor
    public ContinentDatabaseRecord(
            @MaxMindDbParameter(name="code") String code,
            @MaxMindDbParameter(name="geoname_id") Long geoNameId,
            @MaxMindDbParameter(name="names") Map<String, String> names
    ) {
        this.code = code;
        this.geoNameId = geoNameId;
        this.names = names;
    }

    public String getCode() {
        return this.code;
    }

    public Long getGeoNameId() {
        return this.geoNameId;
    }

    public Map<String, String> getNames() {
        return this.names;
    }
}
