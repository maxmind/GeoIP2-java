package com.maxmind.geoip2.record;

import java.lang.*;
import java.util.*;
import java.io.*;
import org.json.*;

public class Location {
    private Double longitude;
    private Double latitude;
    private String postalCode;
    private Integer postalConfidence;
    private String timeZone;
    private Integer metroCode;
    private Integer accuracyRadius;

    public Location(JSONObject json) {
        try {
            postalCode = json.optString("postal_code", null);
            if (json.has("postal_confidence")) {
                postalConfidence = new Integer(json.getInt("postal_confidence"));
            }
            timeZone = json.optString("time_zone", null);
            if (json.has("longitude")) {
                longitude = new Double(json.getDouble("longitude"));
            }
            if (json.has("latitude")) {
                latitude = new Double(json.getDouble("latitude"));
            }
            if (json.has("metro_code")) {
                metroCode = new Integer(json.getInt("metro_code"));
            }
            if (json.has("accuracy_radius")) {
                accuracyRadius = new Integer(json.getInt("accuracy_radius"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

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
