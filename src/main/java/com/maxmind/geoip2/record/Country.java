package com.maxmind.geoip2.record;

import com.google.api.client.util.Key;

public class Country extends RecordWithNames {
    @Key
    private Integer confidence;

    @Key("iso_code")
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
