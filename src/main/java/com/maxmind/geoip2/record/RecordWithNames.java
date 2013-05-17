package com.maxmind.geoip2.record;

import java.util.HashMap;
import java.util.Map;

import com.google.api.client.util.Key;

public abstract class RecordWithNames {
    @Key
    private HashMap<String, String> names = new HashMap<String, String>();
    @Key("geoname_id")
    private Integer geoNameId;

    protected RecordWithNames() {
    }

    public String getName(String... languages) {
        if (languages.length == 0) {
            return this.names.get("en");
        }

        for (String lang : languages) {
            if (this.names.containsKey(lang)) {
                return this.names.get(lang);
            }
        }
        return null;
    }

    public Map<String, String> getNames() {
        return (Map<String, String>) this.names.clone();
    }

    public Integer getGeoNameId() {
        return this.geoNameId;
    }
}
