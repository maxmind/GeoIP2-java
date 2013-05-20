package com.maxmind.geoip2.record;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RepresentedCountry extends Country {
    @JsonProperty
    private String type;

    public RepresentedCountry() {
    }

    public String getType() {
        return this.type;
    }
}
