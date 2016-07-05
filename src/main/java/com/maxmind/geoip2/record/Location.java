package com.maxmind.geoip2.record;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <p>
 * Contains data for the location record associated with an IP address.
 * </p>
 */
public class Location extends AbstractRecord {

    private final Integer accuracyRadius;
    private final Integer averageIncome;
    private final Double latitude;
    private final Double longitude;
    private final Integer metroCode;
    private final Integer populationDensity;
    private final String timeZone;

    public Location() {
        this(null, null, null, null, null, null, null);
    }

    public Location(
            @JsonProperty("accuracy_radius") Integer accuracyRadius,
            @JsonProperty("average_income") Integer averageIncome,
            @JsonProperty("latitude") Double latitude,
            @JsonProperty("longitude") Double longitude,
            @JsonProperty("metro_code") Integer metroCode,
            @JsonProperty("population_density") Integer populationDensity,
            @JsonProperty("time_zone") String timeZone
    ) {
        this.accuracyRadius = accuracyRadius;
        this.averageIncome = averageIncome;
        this.latitude = latitude;
        this.longitude = longitude;
        this.metroCode = metroCode;
        this.populationDensity = populationDensity;
        this.timeZone = timeZone;
    }

    /**
     * @return The average income in US dollars associated with the requested
     * IP address. This attribute is only available from the Insights end point.
     */
    @JsonProperty("average_income")
    public Integer getAverageIncome() {
        return this.averageIncome;
    }

    /**
     * @return The estimated population per square kilometer associated with the
     * IP address. This attribute is only available from the Insights end point.
     */
    @JsonProperty("population_density")
    public Integer getPopulationDensity() {
        return this.populationDensity;
    }

    /**
     * @return The time zone associated with location, as specified by the <a
     * href="http://www.iana.org/time-zones">IANA Time Zone
     * Database</a>, e.g., "America/New_York".
     */
    @JsonProperty("time_zone")
    public String getTimeZone() {
        return this.timeZone;
    }

    /**
     * @return The approximate accuracy radius in kilometers around the
     * latitude and longitude for the IP address. This is the radius where we
     * have a 67% confidence that the device using the IP address resides
     * within the circle centered at the latitude and longitude with the
     * provided radius.
     */
    @JsonProperty("accuracy_radius")
    public Integer getAccuracyRadius() {
        return this.accuracyRadius;
    }

    /**
     * @return The metro code of the location if the location is in the US.
     * MaxMind returns the same metro codes as the <a href=
     * "https://developers.google.com/adwords/api/docs/appendix/cities-DMAregions"
     * >Google AdWords API</a>.
     */
    @JsonProperty("metro_code")
    public Integer getMetroCode() {
        return this.metroCode;
    }

    /**
     * @return The approximate latitude of the location associated with the
     * IP address. This value is not precise and should not be used to
     * identify a particular address or household.
     */
    public Double getLatitude() {
        return this.latitude;
    }

    /**
     * @return The approximate longitude of the location associated with the
     * IP address. This value is not precise and should not be used to
     * identify a particular address or household.
     */
    public Double getLongitude() {
        return this.longitude;
    }
}
