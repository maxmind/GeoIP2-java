package com.maxmind.geoip2.model;

import com.maxmind.db.MaxMindDbConstructor;
import com.maxmind.db.MaxMindDbParameter;
import com.maxmind.geoip2.record.CityDatabaseRecord;
import com.maxmind.geoip2.record.ContinentDatabaseRecord;
import com.maxmind.geoip2.record.CountryDatabaseRecord;
import com.maxmind.geoip2.record.Location;
import com.maxmind.geoip2.record.Postal;
import com.maxmind.geoip2.record.RepresentedCountryDatabaseRecord;
import com.maxmind.geoip2.record.SubdivisionDatabaseRecord;
import com.maxmind.geoip2.record.TraitsDatabaseRecord;

import java.util.ArrayList;

public class CityDatabaseModel {
    private final CityDatabaseRecord city;
    private final ContinentDatabaseRecord continent;
    private final CountryDatabaseRecord country;
    private final Location location;
    private final Postal postal;
    private final CountryDatabaseRecord registeredCountry;
    private final RepresentedCountryDatabaseRecord representedCountry;
    private final ArrayList<SubdivisionDatabaseRecord> subdivisions;
    private final TraitsDatabaseRecord traits;

    @MaxMindDbConstructor
    public CityDatabaseModel(
            @MaxMindDbParameter(name="city") CityDatabaseRecord city,
            @MaxMindDbParameter(name="continent") ContinentDatabaseRecord continent,
            @MaxMindDbParameter(name="country") CountryDatabaseRecord country,
            @MaxMindDbParameter(name="location") Location location,
            @MaxMindDbParameter(name="postal") Postal postal,
            @MaxMindDbParameter(name="registered_country") CountryDatabaseRecord registeredCountry,
            @MaxMindDbParameter(name="represented_country") RepresentedCountryDatabaseRecord representedCountry,
            @MaxMindDbParameter(name="subdivisions") ArrayList<SubdivisionDatabaseRecord> subdivisions,
            @MaxMindDbParameter(name="traits") TraitsDatabaseRecord traits
    ) {
        this.city = city;
        this.continent = continent;
        this.country = country;
        this.location = location;
        this.postal = postal;
        this.registeredCountry = registeredCountry;
        this.representedCountry = representedCountry;
        this.subdivisions = subdivisions;
        this.traits = traits;
    }

    public CityDatabaseRecord getCity() {
        return this.city;
    }

    public ContinentDatabaseRecord getContinent() {
        return this.continent;
    }

    public CountryDatabaseRecord getCountry() {
        return this.country;
    }

    public Location getLocation() {
        return this.location;
    }

    public Postal getPostal() {
        return this.postal;
    }

    public CountryDatabaseRecord getRegisteredCountry() {
        return this.registeredCountry;
    }

    public RepresentedCountryDatabaseRecord getRepresentedCountry() {
        return this.representedCountry;
    }

    public ArrayList<SubdivisionDatabaseRecord> getSubdivisions() {
        return this.subdivisions;
    }

    public TraitsDatabaseRecord getTraits() {
        return this.traits;
    }
}
