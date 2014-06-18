package com.maxmind.geoip2.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Location;
import com.maxmind.geoip2.record.Postal;
import com.maxmind.geoip2.record.Subdivision;

abstract class AbstractCityResponse extends AbstractCountryResponse {
    @JsonProperty
    private City city = new City();
    @JsonProperty
    private Location location = new Location();
    @JsonProperty
    private Postal postal = new Postal();

    @JsonProperty("subdivisions")
    private ArrayList<Subdivision> subdivisions = new ArrayList<Subdivision>();

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
    public List<Subdivision> getSubdivisions() {
        return new ArrayList<Subdivision>(this.subdivisions);
    }

    /**
     * @return An object representing the most specific subdivision returned. If
     *         the response did not contain any subdivisions, this method
     *         returns an empty {@link Subdivision} object.
     */
    @JsonIgnore
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
                + (this.getSubdivisions() != null ? "getSubdivisionsList()="
                        + this.getSubdivisions() + ", " : "")
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
