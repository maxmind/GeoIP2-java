package com.maxmind.geoip2.model;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.maxmind.db.MaxMindDbConstructor;
import com.maxmind.db.MaxMindDbParameter;
import com.maxmind.db.Network;
import com.maxmind.geoip2.record.*;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * This class provides a model for the data returned by the GeoIP2 Precision:
 * City end point and the GeoIP2 City database.
 * </p>
 * <p>
 * The only difference between the City and Insights model classes is which
 * fields in each record may be populated.
 * </p>
 * <p>
 *
 * @see <a href="https://dev.maxmind.com/geoip/geoip2/web-services">GeoIP2 Web
 * Services</a>
 * </p>
 */
public final class CityResponse extends AbstractCityResponse {

    CityResponse() {
        this(null, null, null, null, null, null, null, null, null, null);
    }

    @MaxMindDbConstructor
    public CityResponse(
            @JsonProperty("city") @MaxMindDbParameter(name="city") City city,
            @JsonProperty("continent") @MaxMindDbParameter(name="continent") Continent continent,
            @JsonProperty("country") @MaxMindDbParameter(name="country") Country country,
            @JsonProperty("location") @MaxMindDbParameter(name="location") Location location,
            @JsonProperty("maxmind") @MaxMindDbParameter(name="maxmind") MaxMind maxmind,
            @JsonProperty("postal") @MaxMindDbParameter(name="postal") Postal postal,
            @JsonProperty("registered_country") @MaxMindDbParameter(name="registered_country") Country registeredCountry,
            @JsonProperty("represented_country") @MaxMindDbParameter(name="represented_country") RepresentedCountry representedCountry,
            @JsonProperty("subdivisions") @MaxMindDbParameter(name="subdivisions") ArrayList<Subdivision> subdivisions,
            @JacksonInject("traits") @JsonProperty("traits") @MaxMindDbParameter(name="traits") Traits traits
    ) {
        super(city, continent, country, location, maxmind, postal, registeredCountry,
                representedCountry, subdivisions, traits);
    }

    public CityResponse(
            CityResponse response,
            String ipAddress,
            Network network,
            List<String> locales
    ) {
        this(
            response.getCity() != null ?
                new City(
                    locales,
                    response.getCity().getConfidence(),
                    response.getCity().getGeoNameId(),
                    response.getCity().getNames()
                ) : null,
            response.getContinent() != null ?
                new Continent(
                    locales,
                    response.getContinent().getCode(),
                    response.getContinent().getGeoNameId(),
                    response.getContinent().getNames()
                ) : null,
            response.getCountry() != null ?
                new Country(
                    locales,
                    response.getCountry().getConfidence(),
                    response.getCountry().getGeoNameId(),
                    response.getCountry().isInEuropeanUnion(),
                    response.getCountry().getIsoCode(),
                    response.getCountry().getNames()
                ) : null,
            response.getLocation(),
            null,
            response.getPostal(),
            response.getRegisteredCountry() != null ?
                new Country(
                    locales,
                    response.getRegisteredCountry().getConfidence(),
                    response.getRegisteredCountry().getGeoNameId(),
                    response.getRegisteredCountry().isInEuropeanUnion(),
                    response.getRegisteredCountry().getIsoCode(),
                    response.getRegisteredCountry().getNames()
                ) : null,
            response.getRepresentedCountry() != null ?
                new RepresentedCountry(
                    locales,
                    response.getRepresentedCountry().getConfidence(),
                    response.getRepresentedCountry().getGeoNameId(),
                    response.getRepresentedCountry().isInEuropeanUnion(),
                    response.getRepresentedCountry().getIsoCode(),
                    response.getRepresentedCountry().getNames(),
                    response.getRepresentedCountry().getType()
                ) : null,
            mapSubdivisions(locales, response.getSubdivisions()),
            response.getTraits() != null ?
                new Traits(
                    response.getTraits().getAutonomousSystemNumber(),
                    response.getTraits().getAutonomousSystemOrganization(),
                    response.getTraits().getConnectionType(),
                    response.getTraits().getDomain(),
                    ipAddress,
                    response.getTraits().isAnonymous(),
                    response.getTraits().isAnonymousProxy(),
                    response.getTraits().isAnonymousVpn(),
                    response.getTraits().isHostingProvider(),
                    response.getTraits().isLegitimateProxy(),
                    response.getTraits().isPublicProxy(),
                    response.getTraits().isSatelliteProvider(),
                    response.getTraits().isTorExitNode(),
                    response.getTraits().getIsp(),
                    network,
                    response.getTraits().getOrganization(),
                    response.getTraits().getUserType(),
                    null,
                    response.getTraits().getStaticIpScore()
                ) :
                new Traits(
                    (Integer) null,
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

    private static ArrayList<Subdivision> mapSubdivisions(
            List<String> locales,
            List<Subdivision> subdivisions
    ) {
        if (subdivisions == null) {
            return null;
        }

        ArrayList<Subdivision> subdivisions2 = new ArrayList<>(subdivisions.size());
        for (Subdivision subdivision : subdivisions) {
            subdivisions2.add(
                new Subdivision(
                    locales,
                    subdivision.getConfidence(),
                    subdivision.getGeoNameId(),
                    subdivision.getIsoCode(),
                    subdivision.getNames()
                )
            );
        }
        return subdivisions2;
    }
}
