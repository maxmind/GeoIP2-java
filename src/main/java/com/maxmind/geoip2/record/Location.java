package com.maxmind.geoip2.record;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maxmind.db.MaxMindDbConstructor;
import com.maxmind.db.MaxMindDbParameter;
import com.maxmind.geoip2.JsonSerializable;

/**
 * <p>
 * Contains data for the location record associated with an IP address.
 * </p>
 *
 * @param accuracyRadius The approximate accuracy radius in kilometers around the
 *                       latitude and longitude for the IP address. This is the radius
 *                       where we have a 67% confidence that the device using the IP
 *                       address resides within the circle centered at the latitude and
 *                       longitude with the provided radius.
 * @param averageIncome The average income in US dollars associated with the requested
 *                      IP address. This attribute is only available from the Insights
 *                      web service.
 * @param latitude The approximate latitude of the location associated with the IP
 *                 address. This value is not precise and should not be used to identify
 *                 a particular address or household.
 * @param longitude The approximate longitude of the location associated with the IP
 *                  address. This value is not precise and should not be used to identify
 *                  a particular address or household.
 * @param populationDensity The estimated population per square kilometer associated with
 *                          the IP address. This attribute is only available from the
 *                          Insights web service.
 * @param timeZone The time zone associated with location, as specified by the
 *                 <a href="https://www.iana.org/time-zones">IANA Time Zone Database</a>,
 *                 e.g., "America/New_York".
 */
public record Location(
    @JsonProperty("accuracy_radius")
    @MaxMindDbParameter(name = "accuracy_radius")
    Integer accuracyRadius,

    @JsonProperty("average_income")
    @MaxMindDbParameter(name = "average_income")
    Integer averageIncome,

    @JsonProperty("latitude")
    @MaxMindDbParameter(name = "latitude")
    Double latitude,

    @JsonProperty("longitude")
    @MaxMindDbParameter(name = "longitude")
    Double longitude,

    @JsonProperty("population_density")
    @MaxMindDbParameter(name = "population_density")
    Integer populationDensity,

    @JsonProperty("time_zone")
    @MaxMindDbParameter(name = "time_zone")
    String timeZone
) implements JsonSerializable {

    /**
     * Compact canonical constructor.
     */
    @MaxMindDbConstructor
    public Location {
    }

    /**
     * Constructs a {@code Location} record with {@code null} values for all the fields.
     */
    public Location() {
        this(null, null, null, null, null, null);
    }

    /**
     * @return The average income in US dollars associated with the requested
     * IP address. This attribute is only available from the Insights web
     * service.
     * @deprecated Use {@link #averageIncome()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    @JsonProperty("average_income")
    public Integer getAverageIncome() {
        return averageIncome();
    }

    /**
     * @return The estimated population per square kilometer associated with the
     * IP address. This attribute is only available from the Insights web
     * service.
     * @deprecated Use {@link #populationDensity()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    @JsonProperty("population_density")
    public Integer getPopulationDensity() {
        return populationDensity();
    }

    /**
     * @return The time zone associated with location, as specified by the <a
     * href="https://www.iana.org/time-zones">IANA Time Zone
     * Database</a>, e.g., "America/New_York".
     * @deprecated Use {@link #timeZone()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    @JsonProperty("time_zone")
    public String getTimeZone() {
        return timeZone();
    }

    /**
     * @return The approximate accuracy radius in kilometers around the
     * latitude and longitude for the IP address. This is the radius where we
     * have a 67% confidence that the device using the IP address resides
     * within the circle centered at the latitude and longitude with the
     * provided radius.
     * @deprecated Use {@link #accuracyRadius()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    @JsonProperty("accuracy_radius")
    public Integer getAccuracyRadius() {
        return accuracyRadius();
    }

    /**
     * @return The approximate latitude of the location associated with the
     * IP address. This value is not precise and should not be used to
     * identify a particular address or household.
     * @deprecated Use {@link #latitude()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    public Double getLatitude() {
        return latitude();
    }

    /**
     * @return The approximate longitude of the location associated with the
     * IP address. This value is not precise and should not be used to
     * identify a particular address or household.
     * @deprecated Use {@link #longitude()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    public Double getLongitude() {
        return longitude();
    }
}
