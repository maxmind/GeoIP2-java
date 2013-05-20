package com.maxmind.geoip2.record;

import com.fasterxml.jackson.annotation.JsonProperty;

public class City extends RecordWithNames {
    @JsonProperty
    private Integer confidence;

    public City() {
        super();
    }

    public Integer getConfidence() {
        return this.confidence;
    }

}
