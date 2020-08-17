package com.maxmind.geoip2.record;

import com.maxmind.db.MaxMindDbConstructor;
import com.maxmind.db.MaxMindDbParameter;

import java.util.Map;

public final class CountryDatabaseRecord {
    private final Long geoNameId;
    private final Boolean isInEuropeanUnion;
    private final String isoCode;
    private final Map<String, String> names;

    @MaxMindDbConstructor
    public CountryDatabaseRecord(
            @MaxMindDbParameter(name="geoname_id") Long geoNameId,
            @MaxMindDbParameter(name="is_in_european_union") Boolean isInEuropeanUnion,
            @MaxMindDbParameter(name="iso_code") String isoCode,
            @MaxMindDbParameter(name="names") Map<String, String> names
    ) {
        this.geoNameId = geoNameId;
        this.isInEuropeanUnion = isInEuropeanUnion;
        this.isoCode = isoCode;
        this.names = names;
    }

    public Long getGeoNameId() {
        return this.geoNameId;
    }

    public boolean getIsInEuropeanUnion() {
        if (this.isInEuropeanUnion == null) {
            return false;
        }
        return this.isInEuropeanUnion;
    }

    public String getIsoCode() {
        return this.isoCode;
    }

    public Map<String, String> getNames() {
        return this.names;
    }
}
