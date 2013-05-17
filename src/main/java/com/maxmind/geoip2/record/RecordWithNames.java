package com.maxmind.geoip2.record;

import java.util.HashMap;

import com.google.api.client.util.Key;

public abstract class RecordWithNames {
    @Key
    private HashMap<String, String> names;
    @Key("geoname_id")
    private Integer geoNameId;

    protected RecordWithNames() {
        this.names = new HashMap<String, String>();
    }

    public String getName(String l) {
        return this.names.get(l);
    }

    public Integer getGeoNameId() {
        return this.geoNameId;
    }
}
