package com.maxmind.geoip2.record;

import com.google.api.client.util.Key;

public class RepresentedCountry extends Country {
    @Key
    private String type;

    public RepresentedCountry() {
    }

    public String getType() {
        return this.type;
    }
}
