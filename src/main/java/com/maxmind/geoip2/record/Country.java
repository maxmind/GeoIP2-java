package com.maxmind.geoip2.record;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Country extends RecordWithNames {
    @JsonProperty
    private Integer confidence;

    @JsonProperty("iso_code")
    private String isoCode;

    public Country() {
        super();
    }

    public String getIsoCode() {
        return this.isoCode;
    }

    public Integer getConfidence() {
        return this.confidence;
    }
}
