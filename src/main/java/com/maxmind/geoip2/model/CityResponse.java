package com.maxmind.geoip2.model;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    public CityResponse(
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

    public CityResponse(
            CityDatabaseModel model,
            String ipAddress,
            Network network,
            List<String> locales
    ) {
        this(
            model.getCity() != null ?
                new City(
                    locales,
                    model.getCity().getConfidence(),
                    model.getCity().getGeoNameId() != null ? model.getCity().getGeoNameId().intValue() : null,
                    model.getCity().getNames()
                ) : null,
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
            model.getLocation(),
            null,
            model.getPostal(),
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
            mapSubdivisions(locales, model.getSubdivisions()),
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
                    null,
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

    private static ArrayList<Subdivision> mapSubdivisions(
            List<String> locales,
            ArrayList<SubdivisionDatabaseRecord> subdivisions
    ) {
        if (subdivisions == null) {
            return null;
        }

        ArrayList<Subdivision> subdivisions2 = new ArrayList<>(subdivisions.size());
        for (SubdivisionDatabaseRecord subdivision : subdivisions) {
            subdivisions2.add(
                new Subdivision(
                    locales,
                    subdivision.getConfidence(),
                    subdivision.getGeoNameId() != null ? subdivision.getGeoNameId().intValue() : null,
                    subdivision.getIsoCode(),
                    subdivision.getNames()
                )
            );
        }
        return subdivisions2;
    }
}
