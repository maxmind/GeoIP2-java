package com.maxmind.geoip2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maxmind.geoip2.record.Continent;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.RepresentedCountry;
import com.maxmind.geoip2.record.Traits;

public class CountryLookup {
    @JsonProperty
    private Continent continent = new Continent();

    @JsonProperty
    private Country country = new Country();

    @JsonProperty("registered_country")
    private Country registeredCountry = new Country();

    @JsonProperty("represented_country")
    private RepresentedCountry representedCountry = new RepresentedCountry();

    @JsonProperty
    private Traits traits = new Traits();

    public CountryLookup() {
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

    @Override
    public String toString() {
        return "CountryLookup ["
                + (this.getContinent() != null ? "getContinent()="
                        + this.getContinent() + ", " : "")
                + (this.getCountry() != null ? "getCountry()="
                        + this.getCountry() + ", " : "")
                + (this.getRegisteredCountry() != null ? "getRegisteredCountry()="
                        + this.getRegisteredCountry() + ", "
                        : "")
                + (this.getRepresentedCountry() != null ? "getRepresentedCountry()="
                        + this.getRepresentedCountry() + ", "
                        : "")
                + (this.getTraits() != null ? "getTraits()=" + this.getTraits()
                        : "") + "]";
    }
}
