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
        return accuracyRadius;
    }

    public Integer getPostalConfidence() {
        return postalConfidence;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Integer getMetroCode() {
        return metroCode;
    }
}
