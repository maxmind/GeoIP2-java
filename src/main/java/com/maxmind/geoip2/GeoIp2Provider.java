package com.maxmind.geoip2;

import java.io.IOException;
import java.net.InetAddress;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.City;
import com.maxmind.geoip2.model.CityIspOrg;
import com.maxmind.geoip2.model.Country;
import com.maxmind.geoip2.model.Omni;

public interface GeoIp2Provider {

    /**
     * @param ipAddress
     *            IPv4 or IPv6 address to lookup.
     * @return A Country model for the requested IP address.
     * @throws GeoIp2Exception
     * @throws IOException
     */
    public Country country(InetAddress ipAddress) throws IOException,
            GeoIp2Exception;

    /**
     * @param ipAddress
     *            IPv4 or IPv6 address to lookup.
     * @return A City model for the requested IP address.
     * @throws GeoIp2Exception
     * @throws IOException
     */
    public City city(InetAddress ipAddress) throws IOException, GeoIp2Exception;

    /**
     * @param ipAddress
     *            IPv4 or IPv6 address to lookup.
     * @return A CityIspOrg model for the requested IP address.
     * @throws GeoIp2Exception
     * @throws IOException
     */
    public CityIspOrg cityIspOrg(InetAddress ipAddress) throws IOException,
            GeoIp2Exception;

    /**
     * @param ipAddress
     *            IPv4 or IPv6 address to lookup.
     * @return An Omni model for the requested IP address.
     * @throws GeoIp2Exception
     * @throws IOException
     */
    public Omni omni(InetAddress ipAddress) throws IOException, GeoIp2Exception;
}
