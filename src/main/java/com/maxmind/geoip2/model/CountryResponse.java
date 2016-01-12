package com.maxmind.geoip2.model;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.maxmind.geoip2.record.*;

/**
 * This class provides a model for the data returned by the GeoIP2 Precision:
 * Country end point.
 *
 * @see <a href="http://dev.maxmind.com/geoip/geoip2/web-services">GeoIP2 Web
 * Services</a>
 */
public final class CountryResponse extends AbstractCountryResponse {

    CountryResponse() {
        this(null, null, null, null, null, null);
    }

    public CountryResponse(
            @JsonProperty("continent") Continent continent,
            @JsonProperty("country") Country country,
            @JsonProperty("maxmind") MaxMind maxmind,
            @JsonProperty("registered_country") Country registeredCountry,
            @JsonProperty("represented_country") RepresentedCountry representedCountry,
            @JacksonInject("traits") @JsonProperty("traits") Traits traits
    ) {
        super(continent, country, maxmind, registeredCountry, representedCountry, traits);
    }
}
