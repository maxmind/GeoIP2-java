package com.maxmind.geoip2.model;

import java.util.ArrayList;

import com.google.api.client.util.Key;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Location;
import com.maxmind.geoip2.record.Postal;
import com.maxmind.geoip2.record.Subdivision;

public class CityResponse extends CountryResponse {
    @Key
    private City city = new City();
    @Key
    private Location location = new Location();
    @Key
    private Postal postal = new Postal();

    @Key("subdivisions")
    private ArrayList<Subdivision> subdivisions = new ArrayList<Subdivision>();

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
