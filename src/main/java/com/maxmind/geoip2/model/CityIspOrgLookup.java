package com.maxmind.geoip2.model;

/**
 * This class provides a model for the data returned by the GeoIP2 City/ISP/Org
 * end point.
 * 
 * The only difference between the City, City/ISP/Org, and Omni model classes is
 * which fields in each record may be populated. See {@link http
 * ://dev.maxmind.com/geoip/geoip2/web-services} more details.
 */
public class CityIspOrgLookup extends CityLookup {
    public CityIspOrgLookup() {
    }
}
