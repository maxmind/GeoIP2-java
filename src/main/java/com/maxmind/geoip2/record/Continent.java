package com.maxmind.geoip2.record;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Continent extends RecordWithNames {
    // FIXME?
    @JsonProperty("continent_code")
    private String continentCode;

    public Continent() {
        super();
    }

    public String getCode() {
        return this.continentCode;
    }
}
