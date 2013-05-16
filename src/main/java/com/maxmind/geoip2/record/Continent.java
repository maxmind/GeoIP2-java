package com.maxmind.geoip2.record;

import com.google.api.client.util.Key;

public class Continent extends RecordWithNames {
    @Key("continent_code")
    private String continentCode;

    public Continent() {
        super();
    }

    public String getCode() {
        return continentCode;
    }
}
