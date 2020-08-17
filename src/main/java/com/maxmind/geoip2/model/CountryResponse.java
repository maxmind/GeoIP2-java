package com.maxmind.geoip2.model;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.maxmind.db.Network;
import com.maxmind.geoip2.record.*;

import java.net.InetAddress;
import java.util.List;

/**
 * This class provides a model for the data returned by the GeoIP2 Precision:
 * Country end point.
 *
 * @see <a href="https://dev.maxmind.com/geoip/geoip2/web-services">GeoIP2 Web
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

    public CountryResponse(
            CountryDatabaseModel model,
            String ipAddress,
            Network network,
            List<String> locales
    ) {
        this(
            model.getContinent() != null ?
                new Continent(
                    locales,
                    model.getContinent().getCode(),
                    model.getContinent().getGeoNameId() != null ? model.getContinent().getGeoNameId().intValue() : null,
                    model.getContinent().getNames()
                ) : null,
            model.getCountry() != null ?
                new Country(
                    locales,
                    model.getCountry().getConfidence(),
                    model.getCountry().getGeoNameId() != null ? model.getCountry().getGeoNameId().intValue() : null,
                    model.getCountry().getIsInEuropeanUnion(),
                    model.getCountry().getIsoCode(),
                    model.getCountry().getNames()
                ) : null,
            null,
            model.getRegisteredCountry() != null ?
                new Country(
                    locales,
                    model.getRegisteredCountry().getConfidence(),
                    model.getRegisteredCountry().getGeoNameId() != null ? model.getRegisteredCountry().getGeoNameId().intValue() : null,
                    model.getRegisteredCountry().getIsInEuropeanUnion(),
                    model.getRegisteredCountry().getIsoCode(),
                    model.getRegisteredCountry().getNames()
                ) : null,
            model.getRepresentedCountry() != null ?
                new RepresentedCountry(
                    locales,
                    model.getRepresentedCountry().getConfidence(),
                    model.getRepresentedCountry().getGeoNameId() != null ? model.getRepresentedCountry().getGeoNameId().intValue() : null,
                    model.getRepresentedCountry().getIsInEuropeanUnion(),
                    model.getRepresentedCountry().getIsoCode(),
                    model.getRepresentedCountry().getNames(),
                    model.getRepresentedCountry().getType()
                ) : null,
            model.getTraits() != null ?
                new Traits(
                    model.getTraits().getAutonomousSystemNumber() != null ? model.getTraits().getAutonomousSystemNumber().intValue() : null,
                    model.getTraits().getAutonomousSystemOrganization(),
                    model.getTraits().getConnectionType(),
                    model.getTraits().getDomain(),
                    ipAddress,
                    model.getTraits().isAnonymous(),
                    model.getTraits().isAnonymousProxy(),
                    model.getTraits().isAnonymousVpn(),
                    model.getTraits().isHostingProvider(),
                    model.getTraits().isLegitimateProxy(),
                    model.getTraits().isPublicProxy(),
                    model.getTraits().isSatelliteProvider(),
                    model.getTraits().isTorExitNode(),
                    model.getTraits().getIsp(),
                    network,
                    model.getTraits().getOrganization(),
                    model.getTraits().getUserType(),
                    model.getTraits().getUserCount(),
                    model.getTraits().getStaticIpScore()
                ) :
                new Traits(
                    null,
                    null,
                    null,
                    null,
                    ipAddress,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    false,
                    null,
                    network,
                    null,
                    null,
                    null,
                    null
                )
        );
    }
}
