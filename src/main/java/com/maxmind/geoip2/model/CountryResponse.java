package com.maxmind.geoip2.model;

import com.google.api.client.util.Key;
import com.maxmind.geoip2.record.Continent;
import com.maxmind.geoip2.record.Traits;

public class CountryResponse {
    @Key
    private com.maxmind.geoip2.record.Country country;
    @Key("registered_country")
    private com.maxmind.geoip2.record.Country registeredCountry;
    @Key
    private Continent continent;
    @Key
    private Traits traits;

    public CountryResponse() {
    }

    public com.maxmind.geoip2.record.Country getCountry() {
        return this.country;
    }

    public com.maxmind.geoip2.record.Country getRegisteredCountry() {
        return this.registeredCountry;
    }

    public Continent getContinent() {
        return this.continent;
    }

    public Traits getTraits() {
        return this.traits;
    }
}
