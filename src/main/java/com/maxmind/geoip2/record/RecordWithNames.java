package com.maxmind.geoip2.record;

import java.util.HashMap;
import java.util.Map;

import com.google.api.client.util.Key;

public abstract class RecordWithNames {
    @Key
    private HashMap<String, String> names = new HashMap<String, String>();
    @Key("geoname_id")
    private Integer geoNameId;

    RecordWithNames() {
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
        return new HashMap<String, String>(this.names);
    }

    public Integer getGeoNameId() {
        return this.geoNameId;
    }
}
