package com.maxmind.geoip2.record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class RecordWithNames {
    @JsonProperty
    private HashMap<String, String> names = new HashMap<String, String>();
    @JsonProperty("geoname_id")
    private Integer geoNameId;

    @JacksonInject("languages")
    public List<String> languages = new ArrayList<String>();

    RecordWithNames() {
    }

    public String getName() {
        for (String lang : this.languages) {
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
