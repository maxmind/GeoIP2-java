package com.maxmind.geoip2.model;

import com.google.api.client.util.Key;
import com.maxmind.geoip2.record.Continent;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.RepresentedCountry;
import com.maxmind.geoip2.record.Traits;

public class CountryResponse {
    @Key
    private Continent continent = new Continent();

    @Key
    private Country country = new Country();

    @Key("registered_country")
    private Country registeredCountry = new Country();

    @Key("represented_country")
    private RepresentedCountry representedCountry = new RepresentedCountry();

    @Key
    private Traits traits = new Traits();

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
