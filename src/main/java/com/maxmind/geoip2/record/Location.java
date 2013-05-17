package com.maxmind.geoip2.record;

import com.google.api.client.util.Key;

public class Location {
    @Key("accuracy_radius")
    private Integer accuracyRadius;

    @Key
    private Double latitude;

    @Key
    private Double longitude;

    @Key("metro_code")
    private Integer metroCode;

    @Key("time_zone")
    private String timeZone;

    public Location() {
    }

    public Integer getAccuracyRadius() {
        return this.accuracyRadius;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public Integer getMetroCode() {
        return this.metroCode;
    }

    public String getTimeZone() {
        return this.timeZone;
    }
}
