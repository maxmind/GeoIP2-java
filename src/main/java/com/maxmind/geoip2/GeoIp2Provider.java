package com.maxmind.geoip2;

import java.io.IOException;
import java.net.InetAddress;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.CityIspOrgResponse;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.model.OmniResponse;

public interface GeoIp2Provider {

    /**
     * @param ipAddress
     *            IPv4 or IPv6 address to lookup.
     * @return A Country model for the requested IP address.
     * @throws GeoIp2Exception
     * @throws IOException
     */
    public CountryResponse country(InetAddress ipAddress) throws IOException,
            GeoIp2Exception;

    /**
     * @param ipAddress
     *            IPv4 or IPv6 address to lookup.
     * @return A City model for the requested IP address.
     * @throws GeoIp2Exception
     * @throws IOException
     */
    public CityResponse city(InetAddress ipAddress) throws IOException, GeoIp2Exception;

    /**
     * @param ipAddress
     *            IPv4 or IPv6 address to lookup.
     * @return A CityIspOrg model for the requested IP address.
     * @throws GeoIp2Exception
     * @throws IOException
     */
    public CityIspOrgResponse cityIspOrg(InetAddress ipAddress) throws IOException,
            GeoIp2Exception;

    /**
     * @param ipAddress
     *            IPv4 or IPv6 address to lookup.
     * @return An Omni model for the requested IP address.
     * @throws GeoIp2Exception
     * @throws IOException
     */
    public OmniResponse omni(InetAddress ipAddress) throws IOException, GeoIp2Exception;
}
