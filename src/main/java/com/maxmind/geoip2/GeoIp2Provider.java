package com.maxmind.geoip2;

import java.io.IOException;
import java.net.InetAddress;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityIspOrgResponse;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.model.OmniResponse;

@SuppressWarnings("deprecation")
public interface GeoIp2Provider {

    /**
     * @param ipAddress
     *            IPv4 or IPv6 address to lookup.
     * @return A Country model for the requested IP address.
     * @throws GeoIp2Exception
     *             if there is an error looking up the IP
     * @throws IOException
     *             if there is an IO error
     */
    public CountryResponse country(InetAddress ipAddress) throws IOException,
            GeoIp2Exception;

    /**
     * @param ipAddress
     *            IPv4 or IPv6 address to lookup.
     * @return A City model for the requested IP address.
     * @throws GeoIp2Exception
     *             if there is an error looking up the IP
     * @throws IOException
     *             if there is an IO error
     */
    public CityResponse city(InetAddress ipAddress) throws IOException,
            GeoIp2Exception;

    /**
     * @param ipAddress
     *            IPv4 or IPv6 address to lookup.
     * @return A CityIspOrg model for the requested IP address.
     * @throws GeoIp2Exception
     *             if there is an error looking up the IP
     * @throws IOException
     *             if there is an IO error
     *
     * @deprecated As of 0.8.0, this has been replaced by {@link #city(InetAddress)}.
     */
    @Deprecated
    public CityIspOrgResponse cityIspOrg(InetAddress ipAddress)
            throws IOException, GeoIp2Exception;

    /**
     * @param ipAddress
     *            IPv4 or IPv6 address to lookup.
     * @return An Omni model for the requested IP address.
     * @throws GeoIp2Exception
     *             if there is an error looking up the IP
     * @throws IOException
     *             if there is an IO error
     *
     * @deprecated As of 0.8.0, use {@link WebServiceClient#insights(InetAddress)} instead.
     */
    @Deprecated
    public OmniResponse omni(InetAddress ipAddress) throws IOException,
            GeoIp2Exception;
}
