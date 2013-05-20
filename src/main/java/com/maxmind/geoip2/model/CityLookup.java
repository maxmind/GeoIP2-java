package com.maxmind.geoip2.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Location;
import com.maxmind.geoip2.record.Postal;
import com.maxmind.geoip2.record.Subdivision;

public class CityLookup extends CountryLookup {
    @JsonProperty
    private City city = new City();
    @JsonProperty
    private Location location = new Location();
    @JsonProperty
    private Postal postal = new Postal();

    @JsonProperty("subdivisions")
    private ArrayList<Subdivision> subdivisions = new ArrayList<Subdivision>();

    public CityLookup() {
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

    public ArrayList<Subdivision> getSubdivisionsList() {
        return new ArrayList<Subdivision>(this.subdivisions);
    }

    public Subdivision getMostSpecificSubdivision() {
        if (this.subdivisions.isEmpty()) {
            return new Subdivision();
        }
        return this.subdivisions.get(this.subdivisions.size() - 1);
    }
}
