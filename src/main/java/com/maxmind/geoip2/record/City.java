package com.maxmind.geoip2.record;

import com.google.api.client.util.Key;

public class City extends RecordWithNames {
    @Key
    private Integer confidence;

    public City() {
        super();
    }

    public Integer getConfidence() {
        return this.confidence;
    }

}
