package com.maxmind.geoip2;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.model.InsightsResponse;
import java.io.IOException;
import java.net.InetAddress;

/**
 * Interface for GeoIP2 web service providers.
 */
public interface WebServiceProvider extends GeoIp2Provider {
    /**
     * @return A Country model for the requesting IP address
     * @throws GeoIp2Exception if there is an error from the web service
     * @throws IOException     if an IO error happens during the request
     */
    CountryResponse country() throws IOException, GeoIp2Exception;

    /**
     * @return A City model for the requesting IP address
     * @throws GeoIp2Exception if there is an error from the web service
     * @throws IOException     if an IO error happens during the request
     */
    CityResponse city() throws IOException, GeoIp2Exception;

    /**
     * @return An Insights model for the requesting IP address
     * @throws GeoIp2Exception if there is an error from the web service
     * @throws IOException     if an IO error happens during the request
     */
    InsightsResponse insights() throws IOException, GeoIp2Exception;

    /**
     * @param ipAddress IPv4 or IPv6 address to lookup.
     * @return An Insight model for the requested IP address.
     * @throws GeoIp2Exception if there is an error looking up the IP
     * @throws IOException     if there is an IO error
     */
    InsightsResponse insights(InetAddress ipAddress) throws IOException,
        GeoIp2Exception;
}
