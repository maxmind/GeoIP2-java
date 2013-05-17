package com.maxmind.geoip2.model;

import java.util.ArrayList;

import com.google.api.client.util.Key;
import com.maxmind.geoip2.record.Location;
import com.maxmind.geoip2.record.RepresentedCountry;
import com.maxmind.geoip2.record.Subdivision;

public class City extends Country {
    private com.maxmind.geoip2.record.City city;
    @Key
    private Location location;
    @Key("represented_country")
    private RepresentedCountry representedCountry;
    @Key("subdivisions")
    private ArrayList<Subdivision> subdivisionsList;

    public City() {
    }

    public com.maxmind.geoip2.record.City getCity() {
        return this.city;
    }

    public Location getLocation() {
        return this.location;
    }

    public RepresentedCountry getRepresentedCountry() {
        return this.representedCountry;
    }

    public ArrayList<Subdivision> getSubdivisionsList() {
        return this.subdivisionsList;
    }
}
