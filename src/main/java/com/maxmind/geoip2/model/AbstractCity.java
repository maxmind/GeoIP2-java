package com.maxmind.geoip2.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maxmind.geoip2.record.CityRecord;
import com.maxmind.geoip2.record.LocationRecord;
import com.maxmind.geoip2.record.PostalRecord;
import com.maxmind.geoip2.record.SubdivisionRecord;

abstract class AbstractCity extends AbstractCountry {
    @JsonProperty
    private CityRecord city = new CityRecord();
    @JsonProperty
    private LocationRecord location = new LocationRecord();
    @JsonProperty
    private PostalRecord postal = new PostalRecord();

    @JsonProperty("subdivisions")
    private ArrayList<SubdivisionRecord> subdivisions = new ArrayList<SubdivisionRecord>();

    /**
     * @return City record for the requested IP address.
     */
    public com.maxmind.geoip2.record.CityRecord getCity() {
        return this.city;
    }

    /**
     * @return Location record for the requested IP address.
     */
    public LocationRecord getLocation() {
        return this.location;
    }

    /**
     * @return the postal
     */
    public PostalRecord getPostal() {
        return this.postal;
    }

    /**
     * @return An {@link List} of {@link SubdivisionRecord} objects representing
     *         the country subdivisions for the requested IP address. The number
     *         and type of subdivisions varies by country, but a subdivision is
     *         typically a state, province, county, etc. Subdivisions are
     *         ordered from most general (largest) to most specific (smallest).
     *         If the response did not contain any subdivisions, this method
     *         returns an empty array.
     */
    public List<SubdivisionRecord> getSubdivisionsList() {
        return new ArrayList<SubdivisionRecord>(this.subdivisions);
    }

    /**
     * @return An object representing the most specific subdivision returned. If
     *         the response did not contain any subdivisions, this method
     *         returns an empty {@link SubdivisionRecord} object.
     */
    public SubdivisionRecord getMostSpecificSubdivision() {
        if (this.subdivisions.isEmpty()) {
            return new SubdivisionRecord();
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
