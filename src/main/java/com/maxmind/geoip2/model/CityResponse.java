package com.maxmind.geoip2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maxmind.geoip2.record.*;

import java.util.ArrayList;

/**
 * <p>
 * This class provides a model for the data returned by the GeoIP2 Precision:
 * City end point.
 * </p>
 * <p>
 * The only difference between the City and Insights model classes is which
 * fields in each record may be populated.
 * </p>
 * <p>
 * @see <a href="http://dev.maxmind.com/geoip/geoip2/web-services">GeoIP2 Web
 * Services</a>
 * </p>
 */
public final class CityResponse extends AbstractCityResponse {

    public CityResponse(@JsonProperty("continent") Continent continent, @JsonProperty("country") Country country,
                        @JsonProperty("registered_country") Country registeredCountry,
                        @JsonProperty("maxmind") MaxMind maxmind,
                        @JsonProperty("represented_country") RepresentedCountry representedCountry,
                        @JsonProperty("traits") Traits traits, @JsonProperty("city") City city, @JsonProperty("location") Location location,
                        @JsonProperty("postal") Postal postal, @JsonProperty("subdivisions") ArrayList<Subdivision> subdivisions) {
        super(maxmind, registeredCountry, traits, country, continent, location, subdivisions, representedCountry, postal, city);
    }
}
