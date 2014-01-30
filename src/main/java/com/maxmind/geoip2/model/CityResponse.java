package com.maxmind.geoip2.model;

/**
 * This class provides a model for the data returned by the GeoIP2 City end
 * point.
 *
 * The only difference between the City, City/ISP/Org, and Omni model classes is
 * which fields in each record may be populated.
 *
 * @see <a href="http://dev.maxmind.com/geoip/geoip2/web-services">GeoIP2 Web
 *      Services</a>
 */
final public class CityResponse extends AbstractCityResponse {
    private static final long serialVersionUID = -7125724130268776656L;

    public CityResponse() {
        // For Jackson
    }
}
