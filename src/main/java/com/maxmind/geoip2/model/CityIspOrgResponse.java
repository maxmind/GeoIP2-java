package com.maxmind.geoip2.model;

/**
 * This class provides a model for the data returned by the GeoIP2 Precision:
 * City end point.
 *
 * The only difference between the City and Insights model classes is
 * which fields in each record may be populated.
 *
 * @see <a href="http://dev.maxmind.com/geoip/geoip2/web-services">GeoIP2 Web
 *      Services</a>
 *
 * @deprecated As of 0.8.0, this has been replaced by {@link CityResponse}.
 */
@Deprecated
final public class CityIspOrgResponse extends AbstractCityResponse {
    public CityIspOrgResponse() {
    }
}
