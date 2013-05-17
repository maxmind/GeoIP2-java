package com.maxmind.geoip2.model;

import java.util.ArrayList;

import com.google.api.client.util.Key;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Location;
import com.maxmind.geoip2.record.Postal;
import com.maxmind.geoip2.record.RepresentedCountry;
import com.maxmind.geoip2.record.Subdivision;

public class CityResponse extends CountryResponse {
    @Key
    private City city;
    @Key
    private Location location;
    @Key
    private Postal postal;
    @Key("represented_country")
    private RepresentedCountry representedCountry;
    @Key("subdivisions")
    private ArrayList<Subdivision> subdivisionsList;

    public CityResponse() {
    }

    public com.maxmind.geoip2.record.City getCity() {
        return this.city;
    }

    public Location getLocation() {
        return this.location;
    }

    /**
     * @return the postal
     */
    public Postal getPostal() {
        return this.postal;
    }

    public RepresentedCountry getRepresentedCountry() {
        return this.representedCountry;
    }

    public ArrayList<Subdivision> getSubdivisionsList() {
        return this.subdivisionsList;
    }
}
