package com.maxmind.geoip2.record;

import com.google.api.client.util.Key;

public class Location {
    @Key
    private Double longitude;
    @Key
    private Double latitude;
    @Key("time_zone")
    private String timeZone;
    @Key("metro_code")
    private Integer metroCode;
    @Key("accuracy_radius")
    private Integer accuracyRadius;

    public Location() {
    }

    public Integer getAccuracyRadius() {
        return this.accuracyRadius;
    }

    public String getTimeZone() {
        return this.timeZone;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public Integer getMetroCode() {
        return this.metroCode;
    }
}
