package com.maxmind.geoip2.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Location;
import com.maxmind.geoip2.record.Postal;
import com.maxmind.geoip2.record.Subdivision;

/**
 * This class provides a model for the data returned by the GeoIP2 City end
 * point.
 * 
 * The only difference between the City, City/ISP/Org, and Omni model classes is
 * which fields in each record may be populated. See {@link http
 * ://dev.maxmind.com/geoip/geoip2/web-services} more details.
 */
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

    /**
     * @return City record for the requested IP address.
     */
    public com.maxmind.geoip2.record.City getCity() {
        return this.city;
    }

    /**
     * @return Location record for the requested IP address.
     */
    public Location getLocation() {
        return this.location;
    }

    /**
     * @return the postal
     */
    public Postal getPostal() {
        return this.postal;
    }

    /**
     * @return An {@link List} of {@link Subdivision} objects representing the
     *         country subdivisions for the requested IP address. The number and
     *         type of subdivisions varies by country, but a subdivision is
     *         typically a state, province, county, etc. Subdivisions are
     *         ordered from most general (largest) to most specific (smallest).
     *         If the response did not contain any subdivisions, this method
     *         returns an empty array.
     */
    public List<Subdivision> getSubdivisionsList() {
        return new ArrayList<Subdivision>(this.subdivisions);
    }

    /**
     * @return An object representing the most specific subdivision returned. If
     *         the response did not contain any subdivisions, this method
     *         returns an empty {@link Subdivision} object.
     */
    public Subdivision getMostSpecificSubdivision() {
        if (this.subdivisions.isEmpty()) {
            return new Subdivision();
        }
        return this.subdivisions.get(this.subdivisions.size() - 1);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName()
                + " ["
                + (this.getCity() != null ? "getCity()=" + this.getCity()
                        + ", " : "")
                + (this.getLocation() != null ? "getLocation()="
                        + this.getLocation() + ", " : "")
                + (this.getPostal() != null ? "getPostal()=" + this.getPostal()
                        + ", " : "")
                + (this.getSubdivisionsList() != null ? "getSubdivisionsList()="
                        + this.getSubdivisionsList() + ", "
                        : "")
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
