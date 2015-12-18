package com.maxmind.geoip2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maxmind.geoip2.record.*;

public abstract class AbstractCountryResponse extends AbstractResponse {

    private final Continent continent;
    private final Country country;
    private final Country registeredCountry;
    private final MaxMind maxmind;
    private final RepresentedCountry representedCountry;
    private final Traits traits;

    AbstractCountryResponse() {
        this(null, null, null, null, null, null);
    }

    AbstractCountryResponse(
            Continent continent,
            Country country,
            MaxMind maxmind,
            Country registeredCountry,
            RepresentedCountry representedCountry,
            Traits traits
    ) {
        this.continent = continent != null ? continent : new Continent();
        this.country = country != null ? country : new Country();
        this.registeredCountry = registeredCountry != null ? registeredCountry : new Country();
        this.maxmind = maxmind != null ? maxmind : new MaxMind();
        this.representedCountry = representedCountry != null ? representedCountry : new RepresentedCountry();
        this.traits = traits != null ? traits : new Traits();
    }

    /**
     * @return MaxMind record containing data related to your account.
     */
    @JsonProperty("maxmind")
    public MaxMind getMaxMind() {
        return this.maxmind;
    }

    /**
     * @return Registered country record for the requested IP address. This
     * record represents the country where the ISP has registered a
     * given IP block and may differ from the user's country.
     */
    @JsonProperty("registered_country")
    public Country getRegisteredCountry() {
        return this.registeredCountry;
    }

    /**
     * @return Continent record for the requested IP address.
     */
    public Continent getContinent() {
        return this.continent;
    }

    /**
     * @return Country record for the requested IP address. This object
     * represents the country where MaxMind believes the end user is
     * located.
     */
    public Country getCountry() {
        return this.country;
    }

    /**
     * @return Represented country record for the requested IP address. The
     * represented country is used for things like military bases. It is
     * only present when the represented country differs from the
     * country.
     */
    @JsonProperty("represented_country")
    public RepresentedCountry getRepresentedCountry() {
        return this.representedCountry;
    }

    /**
     * @return Record for the traits of the requested IP address.
     */
    public Traits getTraits() {
        return this.traits;
    }
}
