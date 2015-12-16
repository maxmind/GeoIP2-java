package com.maxmind.geoip2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maxmind.geoip2.record.*;

import java.util.List;

/**
 * <p>
 * This class provides a model for the data returned by the GeoIP2 Precision:
 * Insights end point.
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
public class InsightsResponse extends AbstractCityResponse {

    public InsightsResponse(@JsonProperty("maxmind") MaxMind maxmind, @JsonProperty("registered_country") Country registeredCountry,
                            @JsonProperty("traits") Traits traits, @JsonProperty("country") Country country,
                            @JsonProperty("continent") Continent continent, @JsonProperty("location") Location location,
                            @JsonProperty("subdivisions") List<Subdivision> subdivisions,
                            @JsonProperty("represented_country") RepresentedCountry representedCountry,
                            @JsonProperty("postal") Postal postal, @JsonProperty("city") City city) {
        super(maxmind, registeredCountry, traits, country, continent, location, subdivisions, representedCountry, postal, city);
    }

}
