package com.maxmind.geoip2.record;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Subdivision extends RecordWithNames {
    @JsonProperty
    private Integer confidence;

    @JsonProperty("iso_code")
    private String isoCode;

    public Subdivision() {
    }

    public Integer getConfidence() {
        return this.confidence;
    }

    public String getIsoCode() {
        return this.isoCode;
    }
}
