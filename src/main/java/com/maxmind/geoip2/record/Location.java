package com.maxmind.geoip2.record;

import com.google.api.client.util.Key;

public class Location {
    @Key
    private Double longitude;
    @Key
    private Double latitude;
    @Key("postal_code")
    private String postalCode;
    @Key("postal_confidence")
    private Integer postalConfidence;
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

    public Integer getPostalConfidence() {
        return this.postalConfidence;
    }

    public String getPostalCode() {
        return this.postalCode;
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
