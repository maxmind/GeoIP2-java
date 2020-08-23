package com.maxmind.geoip2;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.CountryResponse;

import java.io.IOException;
import java.net.InetAddress;

public interface GeoIp2Provider {

    /**
     * @param ipAddress IPv4 or IPv6 address to lookup.
     * @return A Country model for the requested IP address.
     * @throws GeoIp2Exception if there is an error looking up the IP
     * @throws IOException     if there is an IO error
     */
    CountryResponse country(InetAddress ipAddress) throws IOException,
            GeoIp2Exception;

    /**
     * @param ipAddress IPv4 or IPv6 address to lookup.
     * @return A City model for the requested IP address.
     * @throws GeoIp2Exception if there is an error looking up the IP
     * @throws IOException     if there is an IO error
     */
    CityResponse city(InetAddress ipAddress) throws IOException,
            GeoIp2Exception;
}
