package com.maxmind.geoip2.model;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.maxmind.db.MaxMindDbParameter;
import com.maxmind.db.Network;
import com.maxmind.geoip2.JsonSerializable;
import com.maxmind.geoip2.record.City;
import com.maxmind.geoip2.record.Continent;
import com.maxmind.geoip2.record.Country;
import com.maxmind.geoip2.record.Location;
import com.maxmind.geoip2.record.MaxMind;
import com.maxmind.geoip2.record.Postal;
import com.maxmind.geoip2.record.RepresentedCountry;
import com.maxmind.geoip2.record.Subdivision;
import com.maxmind.geoip2.record.Traits;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides a model for the data returned by the City Plus web
 * service and the City database.
 *
 * @param city City record for the requested IP address.
 * @param continent Continent record for the requested IP address.
 * @param country Country record for the requested IP address. This object represents the country
 *                where MaxMind believes the end user is located.
 * @param location Location record for the requested IP address.
 * @param maxmind MaxMind record containing data related to your account.
 * @param postal Postal record for the requested IP address.
 * @param registeredCountry Registered country record for the requested IP address. This record
 *                          represents the country where the ISP has registered a given IP block
 *                          and may differ from the user's country.
 * @param representedCountry Represented country record for the requested IP address. The
 *                           represented country is used for things like military bases. It is
 *                           only present when the represented country differs from the country.
 * @param subdivisions An {@link List} of {@link Subdivision} objects representing the country
 *                     subdivisions for the requested IP address. The number and type of
 *                     subdivisions varies by country, but a subdivision is typically a state,
 *                     province, county, etc. Subdivisions are ordered from most general (largest)
 *                     to most specific (smallest). If the response did not contain any
 *                     subdivisions, this is an empty list.
 * @param traits Record for the traits of the requested IP address.
 * @see <a href="https://dev.maxmind.com/geoip/docs/web-services?lang=en">GeoIP2 Web
 * Services</a>
 */
public record CityResponse(
    @JsonProperty("city")
    @MaxMindDbParameter(name = "city")
    City city,

    @JsonProperty("continent")
    @MaxMindDbParameter(name = "continent")
    Continent continent,

    @JsonProperty("country")
    @MaxMindDbParameter(name = "country")
    Country country,

    @JsonProperty("location")
    @MaxMindDbParameter(name = "location")
    Location location,

    @JsonProperty("maxmind")
    @MaxMindDbParameter(name = "maxmind")
    MaxMind maxmind,

    @JsonProperty("postal")
    @MaxMindDbParameter(name = "postal")
    Postal postal,

    @JsonProperty("registered_country")
    @MaxMindDbParameter(name = "registered_country")
    Country registeredCountry,

    @JsonProperty("represented_country")
    @MaxMindDbParameter(name = "represented_country")
    RepresentedCountry representedCountry,

    @JsonProperty("subdivisions")
    @MaxMindDbParameter(name = "subdivisions")
    List<Subdivision> subdivisions,

    @JsonProperty("traits")
    @MaxMindDbParameter(name = "traits")
    Traits traits
) implements JsonSerializable {

    /**
     * Compact canonical constructor that sets defaults for null values.
     */
    public CityResponse {
        city = city != null ? city : new City();
        continent = continent != null ? continent : new Continent();
        country = country != null ? country : new Country();
        location = location != null ? location : new Location();
        maxmind = maxmind != null ? maxmind : new MaxMind();
        postal = postal != null ? postal : new Postal();
        registeredCountry = registeredCountry != null ? registeredCountry : new Country();
        representedCountry = representedCountry != null
            ? representedCountry : new RepresentedCountry();
        subdivisions = subdivisions != null ? List.copyOf(subdivisions) : List.of();
        traits = traits != null ? traits : new Traits();
    }

    /**
     * Constructs an instance of {@code CityResponse} with the specified parameters.
     *
     * @param response the response
     * @param ipAddress the IP address that the data in the model is for.
     * @param network the network associated with the record.
     * @param locales the locales
     */
    public CityResponse(
        CityResponse response,
        String ipAddress,
        Network network,
        List<String> locales
    ) {
        this(
            new City(response.city(), locales),
            new Continent(response.continent(), locales),
            new Country(response.country(), locales),
            response.location(),
            response.maxmind(),
            response.postal(),
            new Country(response.registeredCountry(), locales),
            new RepresentedCountry(response.representedCountry(), locales),
            mapSubdivisions(response.subdivisions(), locales),
            new Traits(response.traits(), ipAddress, network)
        );
    }

    private static ArrayList<Subdivision> mapSubdivisions(
        List<Subdivision> subdivisions,
        List<String> locales
    ) {
        ArrayList<Subdivision> subdivisions2 = new ArrayList<>(subdivisions.size());
        for (Subdivision subdivision : subdivisions) {
            subdivisions2.add(new Subdivision(subdivision, locales));
        }
        return subdivisions2;
    }

    /**
     * @return City record for the requested IP address.
     * @deprecated Use {@link #city()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    public City getCity() {
        return city();
    }

    /**
     * @return Continent record for the requested IP address.
     * @deprecated Use {@link #continent()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    public Continent getContinent() {
        return continent();
    }

    /**
     * @return Country record for the requested IP address. This object
     * represents the country where MaxMind believes the end user is
     * located.
     * @deprecated Use {@link #country()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    public Country getCountry() {
        return country();
    }

    /**
     * @return Location record for the requested IP address.
     * @deprecated Use {@link #location()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    public Location getLocation() {
        return location();
    }

    /**
     * @return MaxMind record containing data related to your account.
     * @deprecated Use {@link #maxmind()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    @JsonProperty("maxmind")
    public MaxMind getMaxMind() {
        return maxmind();
    }

    /**
     * @return the postal
     * @deprecated Use {@link #postal()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    public Postal getPostal() {
        return postal();
    }

    /**
     * @return Registered country record for the requested IP address. This
     * record represents the country where the ISP has registered a
     * given IP block and may differ from the user's country.
     * @deprecated Use {@link #registeredCountry()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    @JsonProperty("registered_country")
    public Country getRegisteredCountry() {
        return registeredCountry();
    }

    /**
     * @return Represented country record for the requested IP address. The
     * represented country is used for things like military bases. It is
     * only present when the represented country differs from the
     * country.
     * @deprecated Use {@link #representedCountry()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    @JsonProperty("represented_country")
    public RepresentedCountry getRepresentedCountry() {
        return representedCountry();
    }

    /**
     * @return An {@link List} of {@link Subdivision} objects representing the
     * country subdivisions for the requested IP address. The number and
     * type of subdivisions varies by country, but a subdivision is
     * typically a state, province, county, etc. Subdivisions are
     * ordered from most general (largest) to most specific (smallest).
     * If the response did not contain any subdivisions, this method
     * returns an empty array.
     * @deprecated Use {@link #subdivisions()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    public List<Subdivision> getSubdivisions() {
        return new ArrayList<>(subdivisions());
    }

    /**
     * @return Record for the traits of the requested IP address.
     * @deprecated Use {@link #traits()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    public Traits getTraits() {
        return traits();
    }

    /**
     * @return An object representing the most specific subdivision returned. If
     * the response did not contain any subdivisions, this method
     * returns an empty {@link Subdivision} object.
     */
    @JsonIgnore
    public Subdivision mostSpecificSubdivision() {
        if (subdivisions().isEmpty()) {
            return new Subdivision();
        }
        return subdivisions().get(subdivisions().size() - 1);
    }

    /**
     * @return An object representing the most specific subdivision returned. If
     * the response did not contain any subdivisions, this method
     * returns an empty {@link Subdivision} object.
     * @deprecated Use {@link #mostSpecificSubdivision()} instead. This method will be removed
     *     in 6.0.0.
     */
    @JsonIgnore
    @Deprecated(since = "5.0.0", forRemoval = true)
    public Subdivision getMostSpecificSubdivision() {
        return mostSpecificSubdivision();
    }

    /**
     * @return An object representing the least specific subdivision returned. If
     * the response did not contain any subdivisions, this method
     * returns an empty {@link Subdivision} object.
     */
    @JsonIgnore
    public Subdivision leastSpecificSubdivision() {
        if (subdivisions().isEmpty()) {
            return new Subdivision();
        }
        return subdivisions().get(0);
    }

    /**
     * @return An object representing the least specific subdivision returned. If
     * the response did not contain any subdivisions, this method
     * returns an empty {@link Subdivision} object.
     * @deprecated Use {@link #leastSpecificSubdivision()} instead. This method will be removed
     *     in 6.0.0.
     */
    @JsonIgnore
    @Deprecated(since = "5.0.0", forRemoval = true)
    public Subdivision getLeastSpecificSubdivision() {
        return leastSpecificSubdivision();
    }
}
