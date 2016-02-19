package com.maxmind.geoip2.model;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.maxmind.geoip2.record.*;

import java.util.ArrayList;

/**
 * <p>
 * This class provides a model for the data returned by the GeoIP2 Enterprise
 * database
 * </p>
 */
public final class EnterpriseResponse extends AbstractCityResponse {

    public EnterpriseResponse(
            @JsonProperty("city") City city,
            @JsonProperty("continent") Continent continent,
            @JsonProperty("country") Country country,
            @JsonProperty("location") Location location,
            @JsonProperty("maxmind") MaxMind maxmind,
            @JsonProperty("postal") Postal postal,
            @JsonProperty("registered_country") Country registeredCountry,
            @JsonProperty("represented_country") RepresentedCountry representedCountry,
            @JsonProperty("subdivisions") ArrayList<Subdivision> subdivisions,
            @JacksonInject("traits") @JsonProperty("traits") Traits traits
    ) {
        super(city, continent, country, location, maxmind, postal, registeredCountry,
                representedCountry, subdivisions, traits);
    }
}
