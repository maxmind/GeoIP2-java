package com.maxmind.geoip2.model;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.maxmind.db.MaxMindDbParameter;
import com.maxmind.db.Network;
import com.maxmind.geoip2.JsonSerializable;
import com.maxmind.geoip2.record.Continent;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.MaxMind;
import com.maxmind.geoip2.record.RepresentedCountry;
import com.maxmind.geoip2.record.Traits;
import java.util.List;

/**
 * This class provides a model for the data returned by the Country web service
 * and the Country database.
 *
 * @param continent Continent record for the requested IP address.
 * @param country Country record for the requested IP address. This object represents the country
 *                where MaxMind believes the end user is located.
 * @param maxmind MaxMind record containing data related to your account.
 * @param registeredCountry Registered country record for the requested IP address. This record
 *                          represents the country where the ISP has registered a given IP block
 *                          and may differ from the user's country.
 * @param representedCountry Represented country record for the requested IP address. The
 *                           represented country is used for things like military bases. It is
 *                           only present when the represented country differs from the country.
 * @param traits Record for the traits of the requested IP address.
 * @see <a href="https://dev.maxmind.com/geoip/docs/web-services?lang=en">GeoIP2 Web
 * Services</a>
 */
public record CountryResponse(
    @JsonProperty("continent")
    @MaxMindDbParameter(name = "continent")
    Continent continent,

    @JsonProperty("country")
    @MaxMindDbParameter(name = "country")
    Country country,

    @JsonProperty("maxmind")
    @MaxMindDbParameter(name = "maxmind")
    MaxMind maxmind,

    @JsonProperty("registered_country")
    @MaxMindDbParameter(name = "registered_country")
    Country registeredCountry,

    @JsonProperty("represented_country")
    @MaxMindDbParameter(name = "represented_country")
    RepresentedCountry representedCountry,

    @JsonProperty("traits")
    @MaxMindDbParameter(name = "traits")
    Traits traits
) implements JsonSerializable {

    /**
     * Compact canonical constructor that sets defaults for null values.
     */
    public CountryResponse {
        continent = continent != null ? continent : new Continent();
        country = country != null ? country : new Country();
        maxmind = maxmind != null ? maxmind : new MaxMind();
        registeredCountry = registeredCountry != null ? registeredCountry : new Country();
        representedCountry = representedCountry != null
            ? representedCountry : new RepresentedCountry();
        traits = traits != null ? traits : new Traits();
    }

    /**
     * Constructs an instance of {@code CountryResponse} with the specified parameters.
     *
     * @param response the response
     * @param locales the locales
     */
    public CountryResponse(
        CountryResponse response,
        List<String> locales
    ) {
        this(
            new Continent(response.continent(), locales),
            new Country(response.country(), locales),
            response.maxmind(),
            new Country(response.registeredCountry(), locales),
            new RepresentedCountry(response.representedCountry(), locales),
            response.traits()
        );
    }

    /**
     * @return MaxMind record containing data related to your account.
     * @deprecated Use {@link #maxmind()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    @JsonProperty("maxmind")
    public MaxMind getMaxMind() {
        return maxmind();
    }

    /**
     * @return Registered country record for the requested IP address. This
     * record represents the country where the ISP has registered a
     * given IP block and may differ from the user's country.
     * @deprecated Use {@link #registeredCountry()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    @JsonProperty("registered_country")
    public Country getRegisteredCountry() {
        return registeredCountry();
    }

    /**
     * @return Continent record for the requested IP address.
     * @deprecated Use {@link #continent()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    public Continent getContinent() {
        return continent();
    }

    /**
     * @return Country record for the requested IP address. This object
     * represents the country where MaxMind believes the end user is
     * located.
     * @deprecated Use {@link #country()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    public Country getCountry() {
        return country();
    }

    /**
     * @return Represented country record for the requested IP address. The
     * represented country is used for things like military bases. It is
     * only present when the represented country differs from the
     * country.
     * @deprecated Use {@link #representedCountry()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    @JsonProperty("represented_country")
    public RepresentedCountry getRepresentedCountry() {
        return representedCountry();
    }

    /**
     * @return Record for the traits of the requested IP address.
     * @deprecated Use {@link #traits()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    public Traits getTraits() {
        return traits();
    }
}
