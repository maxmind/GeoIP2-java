package com.maxmind.geoip2.record;

import com.maxmind.db.MaxMindDbConstructor;
import com.maxmind.db.MaxMindDbParameter;

import java.util.Map;

public final class SubdivisionDatabaseRecord {
    private final Long geoNameId;
    private final String isoCode;
    private final Map<String, String> names;

    @MaxMindDbConstructor
    public SubdivisionDatabaseRecord(
            @MaxMindDbParameter(name="geoname_id") Long geoNameId,
            @MaxMindDbParameter(name="iso_code") String isoCode,
            @MaxMindDbParameter(name="names") Map<String, String> names
    ) {
        this.geoNameId = geoNameId;
        this.isoCode = isoCode;
        this.names = names;
    }

    public Long getGeoNameId() {
        return this.geoNameId;
    }

    public String getIsoCode() {
        return this.isoCode;
    }

    public Map<String, String> getNames() {
        return this.names;
    }
}
