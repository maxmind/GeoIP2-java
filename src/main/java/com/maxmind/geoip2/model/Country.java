package com.maxmind.geoip2.model;

import com.maxmind.geoip2.record.*;
import com.google.api.client.util.Key;

public class Country {
    @Key
    private com.maxmind.geoip2.record.Country country;
    @Key("registered_country")
    private com.maxmind.geoip2.record.Country registeredCountry;
    @Key
    private Continent continent;
    @Key
    private Traits traits;

    public Country(){}
  
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
