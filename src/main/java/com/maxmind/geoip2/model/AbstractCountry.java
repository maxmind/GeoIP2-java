package com.maxmind.geoip2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maxmind.geoip2.record.ContinentRecord;
import com.maxmind.geoip2.record.CountryRecord;
import com.maxmind.geoip2.record.MaxMindRecord;
import com.maxmind.geoip2.record.RepresentedCountryRecord;
import com.maxmind.geoip2.record.TraitsRecord;

abstract class AbstractCountry {
    @JsonProperty
    private ContinentRecord continent = new ContinentRecord();

    @JsonProperty
    private CountryRecord country = new CountryRecord();

    @JsonProperty("registered_country")
    private CountryRecord registeredCountry = new CountryRecord();

    @JsonProperty("maxmind")
    private MaxMindRecord maxmind = new MaxMindRecord();

    @JsonProperty("represented_country")
    private RepresentedCountryRecord representedCountry = new RepresentedCountryRecord();

    @JsonProperty
    private TraitsRecord traits = new TraitsRecord();

    /**
     * @return Continent record for the requested IP address.
     */
    public ContinentRecord getContinent() {
        return this.continent;
    }

    /**
     * @return Country record for the requested IP address. This object
     *         represents the country where MaxMind believes the end user is
     *         located.
     */
    public CountryRecord getCountry() {
        return this.country;
    }

    /**
     * @return MaxMind record containing data related to your account.
     */
    public MaxMindRecord getMaxMind() {
        return this.maxmind;
    }

    /**
     * @return Registered country record for the requested IP address. This
     *         record represents the country where the ISP has registered a
     *         given IP block and may differ from the user's country.
     */
    public CountryRecord getRegisteredCountry() {
        return this.registeredCountry;
    }

    /**
     * @return Represented country record for the requested IP address. The
     *         represented country is used for things like military bases or
     *         embassies. It is only present when the represented country
     *         differs from the country.
     */
    public RepresentedCountryRecord getRepresentedCountry() {
        return this.representedCountry;
    }

    /**
     * @return Record for the traits of the requested IP address.
     */
    public TraitsRecord getTraits() {
        return this.traits;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Country ["
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
