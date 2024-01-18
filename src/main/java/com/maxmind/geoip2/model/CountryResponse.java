package com.maxmind.geoip2.model;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.maxmind.db.MaxMindDbConstructor;
import com.maxmind.db.MaxMindDbParameter;
import com.maxmind.db.Network;
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
 * @see <a href="https://dev.maxmind.com/geoip/docs/web-services?lang=en">GeoIP2 Web
 * Services</a>
 */
public final class CountryResponse extends AbstractCountryResponse {

    /**
     * Constructs an instance of {@code CountryResponse} with the specified parameters.
     *
     * @param continent the continent
     * @param country the country
     * @param maxmind the MaxMind record
     * @param registeredCountry the registered country
     * @param representedCountry the represented country
     * @param traits the traits
     */
    @MaxMindDbConstructor
    public CountryResponse(
        @JsonProperty("continent") @MaxMindDbParameter(name = "continent") Continent continent,
        @JsonProperty("country") @MaxMindDbParameter(name = "country") Country country,
        @JsonProperty("maxmind") @MaxMindDbParameter(name = "maxmind") MaxMind maxmind,
        @JsonProperty("registered_country") @MaxMindDbParameter(name = "registered_country")
        Country registeredCountry,
        @JsonProperty("represented_country") @MaxMindDbParameter(name = "represented_country")
        RepresentedCountry representedCountry,
        @JacksonInject("traits") @JsonProperty("traits") @MaxMindDbParameter(name = "traits")
        Traits traits
    ) {
        super(continent, country, maxmind, registeredCountry, representedCountry, traits);
    }

    /**
     * Constructs an instance of {@code CountryResponse} with the specified parameters.
     *
     * @param response the response
     * @param ipAddress the IP address that the data in the model is for.
     * @param network the network associated with the record.
     * @param locales the locales
     */
    public CountryResponse(
        CountryResponse response,
        String ipAddress,
        Network network,
        List<String> locales
    ) {
        super(response, ipAddress, network, locales);
    }
}
