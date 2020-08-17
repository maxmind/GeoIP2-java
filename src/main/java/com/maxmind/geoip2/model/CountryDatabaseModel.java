package com.maxmind.geoip2.model;

import com.maxmind.db.MaxMindDbConstructor;
import com.maxmind.db.MaxMindDbParameter;
import com.maxmind.geoip2.record.ContinentDatabaseRecord;
import com.maxmind.geoip2.record.CountryDatabaseRecord;
import com.maxmind.geoip2.record.RepresentedCountryDatabaseRecord;
import com.maxmind.geoip2.record.TraitsDatabaseRecord;

public class CountryDatabaseModel {
    private final ContinentDatabaseRecord continent;
    private final CountryDatabaseRecord country;
    private final CountryDatabaseRecord registeredCountry;
    private final RepresentedCountryDatabaseRecord representedCountry;
    private final TraitsDatabaseRecord traits;

    @MaxMindDbConstructor
    public CountryDatabaseModel(
            @MaxMindDbParameter(name="continent") ContinentDatabaseRecord continent,
            @MaxMindDbParameter(name="country") CountryDatabaseRecord country,
            @MaxMindDbParameter(name="registered_country") CountryDatabaseRecord registeredCountry,
            @MaxMindDbParameter(name="represented_country") RepresentedCountryDatabaseRecord representedCountry,
            @MaxMindDbParameter(name="traits") TraitsDatabaseRecord traits
    ) {
        this.continent = continent;
        this.country = country;
        this.registeredCountry = registeredCountry;
        this.representedCountry = representedCountry;
        this.traits = traits;
    }

    public ContinentDatabaseRecord getContinent() {
        return this.continent;
    }

    public CountryDatabaseRecord getCountry() {
        return this.country;
    }

    public CountryDatabaseRecord getRegisteredCountry() {
        return this.registeredCountry;
    }

    public RepresentedCountryDatabaseRecord getRepresentedCountry() {
        return this.representedCountry;
    }

    public TraitsDatabaseRecord getTraits() {
        return this.traits;
    }
}
