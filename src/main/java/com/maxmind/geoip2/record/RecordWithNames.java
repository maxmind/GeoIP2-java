package com.maxmind.geoip2.record;

import java.util.HashMap;
import com.google.api.client.util.Key;

public abstract class RecordWithNames {
    @Key
    private HashMap<String, String> names;
    @Key("geoname_id")
    private Integer geoNameId;
    @Key
    private Integer confidence;

    protected RecordWithNames() {
        names = new HashMap<String, String>();
    }

    public String getName(String l) {
        return names.get(l);
    }

    public int getGeoNameId() {
        return geoNameId;
    }

    public Integer getConfidence() {
        return confidence;
    }
}
