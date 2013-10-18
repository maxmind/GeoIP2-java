package com.maxmind.geoip2.record;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Contains data for the location record associated with an IP address.
 *
 * This record is returned by all the end points except the Country end point.
 */
final public class Location {
    @JsonProperty("accuracy_radius")
    private Integer accuracyRadius;

    @JsonProperty
    private Double latitude;

    @JsonProperty
    private Double longitude;

    @JsonProperty("metro_code")
    private Integer metroCode;

    @JsonProperty("time_zone")
    private String timeZone;

    public Location() {
    }

    /**
     * @return The radius in kilometers around the specified location where the
     *         IP address is likely to be. This attribute is only available from
     *         the Omni end point.
     */
    public Integer getAccuracyRadius() {
        return this.accuracyRadius;
    }

    /**
     * @return The latitude of the location as a floating point number. This
     *         attribute is returned by all end points except the Country end
     *         point.
     */
    public Double getLatitude() {
        return this.latitude;
    }

    /**
     * @return The longitude of the location as a floating point number. This
     *         attribute is returned by all end points except the Country end
     *         point.
     */
    public Double getLongitude() {
        return this.longitude;
    }

    /**
     * @return The metro code of the location if the location is in the US.
     *         MaxMind returns the same metro codes as the <a href=
     *         "https://developers.google.com/adwords/api/docs/appendix/cities-DMAregions"
     *         >Google AdWords API</a>. This attribute is returned by all end
     *         points except the Country end point.
     */
    public Integer getMetroCode() {
        return this.metroCode;
    }

    /**
     * @return The time zone associated with location, as specified by the <a
     *         href="http://www.iana.org/time-zones">IANA Time Zone
     *         Database</a>, e.g., "America/New_York". This attribute is
     *         returned by all end points except the Country end point.
     */
    public String getTimeZone() {
        return this.timeZone;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Location ["
                + (this.accuracyRadius != null ? "accuracyRadius="
                        + this.accuracyRadius + ", " : "")
                + (this.latitude != null ? "latitude=" + this.latitude + ", "
                        : "")
                + (this.longitude != null ? "longitude=" + this.longitude
                        + ", " : "")
                + (this.metroCode != null ? "metroCode=" + this.metroCode
                        + ", " : "")
                + (this.timeZone != null ? "timeZone=" + this.timeZone : "")
                + "]";
    }
}
