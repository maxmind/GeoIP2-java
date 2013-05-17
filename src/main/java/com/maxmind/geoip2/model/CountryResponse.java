package com.maxmind.geoip2.model;

import com.google.api.client.util.Key;
import com.maxmind.geoip2.record.Continent;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.RepresentedCountry;
import com.maxmind.geoip2.record.Traits;

public class CountryResponse {
    @Key
    private Continent continent;

    @Key
    private com.maxmind.geoip2.record.Country country;

    @Key("registered_country")
    private com.maxmind.geoip2.record.Country registeredCountry;

    @Key("represented_country")
    private RepresentedCountry representedCountry;

    @Key
    private Traits traits;

    public CountryResponse() {
    }

    public Continent getContinent() {
        return this.continent;
    }

    public Country getCountry() {
        return this.country;
    }

    public Country getRegisteredCountry() {
        return this.registeredCountry;
    }

    public RepresentedCountry getRepresentedCountry() {
        return this.representedCountry;
    }

    public Traits getTraits() {
        return this.traits;
    }
}
