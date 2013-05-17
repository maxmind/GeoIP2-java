package com.maxmind.geoip2.record;

import com.google.api.client.util.Key;

public class Subdivision extends RecordWithNames {
    @Key
    private Integer confidence;

    @Key("iso_code")
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
