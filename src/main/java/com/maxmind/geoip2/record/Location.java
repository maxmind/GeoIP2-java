package com.maxmind.geoip2.record;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Location {
    @JsonProperty("accuracy_radius")
    private Integer accuracyRadius;

    @JsonProperty
    private Double latitude;

    @JsonProperty
    private Double longitude;

    @JsonProperty("metro_code")
    private Integer metroCode;

    @JsonProperty("time_zone")
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

    @Override
    public String toString() {
        return "Location ["
                + (this.accuracyRadius != null ? "accuracyRadius="
                        + this.accuracyRadius + ", " : "")
                + (this.latitude != null ? "latitude=" + this.latitude + ", "
                        : "")
                + (this.longitude != null ? "longitude=" + this.longitude
                        + ", " : "")
                + (this.metroCode != null ? "metroCode=" + this.metroCode
                        + ", " : "")
                + (this.timeZone != null ? "timeZone=" + this.timeZone : "")
                + "]";
    }
}
