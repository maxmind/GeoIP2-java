package com.maxmind.geoip2.model;

import org.json.*;
import com.maxmind.geoip2.record.*;

public class Country {
    private com.maxmind.geoip2.record.Country country;
    private com.maxmind.geoip2.record.Country registeredCountry;
    private Continent continent;
    private Traits traits;

    public Country(JSONObject json) {
        try {
            JSONObject jcontinent = json.getJSONObject("continent");
            continent = new Continent(jcontinent);
        } catch (JSONException e) {
            continent = new Continent();
        }

        try {
            JSONObject jcountry = json.getJSONObject("country");
            country = new com.maxmind.geoip2.record.Country(jcountry);
        } catch (JSONException e) {
            country = new com.maxmind.geoip2.record.Country();
        }

        try {
            JSONObject jcountry = json.getJSONObject("registered_country");
            registeredCountry = new com.maxmind.geoip2.record.Country(jcountry);
        } catch (JSONException e) {
            registeredCountry = new com.maxmind.geoip2.record.Country();
        }

        try {
            JSONObject jtraits = json.getJSONObject("traits");
            traits = new Traits(jtraits);
        } catch (JSONException e) {
            traits = new Traits();
        }
    }

    public com.maxmind.geoip2.record.Country getCountry() {
        return country;
    }

    public com.maxmind.geoip2.record.Country getRegisteredCountry() {
        return registeredCountry;
    }

    public Continent getContinent() {
        return continent;
    }

    public Traits getTraits() {
        return traits;
    }
}
