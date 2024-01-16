package com.maxmind.geoip2.model;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Continent;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import com.maxmind.geoip2.record.MaxMind;
import com.maxmind.geoip2.record.Postal;
import com.maxmind.geoip2.record.RepresentedCountry;
import com.maxmind.geoip2.record.Subdivision;
import com.maxmind.geoip2.record.Traits;
import java.util.List;

/**
 * This class provides a model for the data returned by the Insights web
 * service.
 *
 * @see <a href="https://dev.maxmind.com/geoip/docs/web-services?lang=en">GeoIP2 Web
 * Services</a>
 */
public class InsightsResponse extends AbstractCityResponse {
    /**
     * Constructs and instance of {@code InsightsResponse} with the specified parameters.
     *
     * @param city city
     * @param continent continent
     * @param country country
     * @param location location
     * @param maxmind maxmind record for the response
     * @param postal postal
     * @param registeredCountry registered country
     * @param representedCountry represented country
     * @param subdivisions subdivisions
     * @param traits traits
     */
    public InsightsResponse(
        @JsonProperty("city") City city,
        @JsonProperty("continent") Continent continent,
        @JsonProperty("country") Country country,
        @JsonProperty("location") Location location,
        @JsonProperty("maxmind") MaxMind maxmind,
        @JsonProperty("postal") Postal postal,
        @JsonProperty("registered_country") Country registeredCountry,
        @JsonProperty("represented_country") RepresentedCountry representedCountry,
        @JsonProperty("subdivisions") List<Subdivision> subdivisions,
        @JacksonInject("traits") @JsonProperty("traits") Traits traits
    ) {
        super(city, continent, country, location, maxmind, postal, registeredCountry,
            representedCountry, subdivisions, traits);
    }
}
