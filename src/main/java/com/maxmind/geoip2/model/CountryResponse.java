package com.maxmind.geoip2.model;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.maxmind.db.MaxMindDbConstructor;
import com.maxmind.db.MaxMindDbParameter;
import com.maxmind.db.Network;
import com.maxmind.geoip2.record.*;

import java.util.List;

/**
 * This class provides a model for the data returned by the GeoIP2 Precision:
 * Country end point.
 *
 * @see <a href="https://dev.maxmind.com/geoip/docs/web-services?lang=en">GeoIP2 Web
 * Services</a>
 */
public final class CountryResponse extends AbstractCountryResponse {
    @MaxMindDbConstructor
    public CountryResponse(
            @JsonProperty("continent") @MaxMindDbParameter(name = "continent") Continent continent,
            @JsonProperty("country") @MaxMindDbParameter(name = "country") Country country,
            @JsonProperty("maxmind") @MaxMindDbParameter(name = "maxmind") MaxMind maxmind,
            @JsonProperty("registered_country") @MaxMindDbParameter(name = "registered_country") Country registeredCountry,
            @JsonProperty("represented_country") @MaxMindDbParameter(name = "represented_country") RepresentedCountry representedCountry,
            @JacksonInject("traits") @JsonProperty("traits") @MaxMindDbParameter(name = "traits") Traits traits
    ) {
        super(continent, country, maxmind, registeredCountry, representedCountry, traits);
    }

    public CountryResponse(
            CountryResponse response,
            String ipAddress,
            Network network,
            List<String> locales
    ) {
        super(response, ipAddress, network, locales);
    }
}
