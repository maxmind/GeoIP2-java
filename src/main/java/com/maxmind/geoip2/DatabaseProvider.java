package com.maxmind.geoip2;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.*;

import java.io.IOException;
import java.net.InetAddress;

public interface DatabaseProvider extends GeoIp2Provider {
    
    /**
     * Same as {@link #country(InetAddress)} but return null when the IP is not in our database.
     * 
     * @param ipAddress
     * @return
     * @throws IOException
     * @throws GeoIp2Exception
     */
    CountryResponse tryCountry(InetAddress ipAddress) throws IOException,
            GeoIp2Exception;
    
    /**
     * Same as {@link #city(InetAddress)} but returns null when the IP is not in our database.
     * 
     * @param ipAddress
     * @return
     * @throws IOException
     * @throws GeoIp2Exception
     */
    public CityResponse tryCity(InetAddress ipAddress) throws IOException,
            GeoIp2Exception;
    
    /**
     * Look up an IP address in a GeoIP2 Anonymous IP.
     *
     * @param ipAddress IPv4 or IPv6 address to lookup.
     * @return a AnonymousIpResponse for the requested IP address.
     * @throws com.maxmind.geoip2.exception.GeoIp2Exception if there is an error looking up the IP
     * @throws java.io.IOException                          if there is an IO error
     */
    AnonymousIpResponse anonymousIp(InetAddress ipAddress) throws IOException,
            GeoIp2Exception;
    
    /**
     * Same as {@link #anonymousIp(InetAddress)} but returns null when the IP is not in our database.
     * 
     * @param ipAddress
     * @return
     * @throws IOException
     * @throws GeoIp2Exception
     */
    AnonymousIpResponse tryAnonymousIp(InetAddress ipAddress) throws IOException,
            GeoIp2Exception;

    /**
     * Look up an IP address in a GeoLite2 ASN database.
     *
     * @param ipAddress IPv4 or IPv6 address to lookup.
     * @return an IspResponse for the requested IP address.
     * @throws com.maxmind.geoip2.exception.GeoIp2Exception if there is an error looking up the IP
     * @throws java.io.IOException                          if there is an IO error
     */
    AsnResponse asn(InetAddress ipAddress) throws IOException,
            GeoIp2Exception;
    
    /**
     * Same as {@link #asn(InetAddress)} but returns null when the IP is not in our database.
     * 
     * @param ipAddress
     * @return
     * @throws IOException
     * @throws GeoIp2Exception
     */
    AsnResponse tryAsn(InetAddress ipAddress) throws IOException,
            GeoIp2Exception;

    /**
     * Look up an IP address in a GeoIP2 Connection Type database.
     *
     * @param ipAddress IPv4 or IPv6 address to lookup.
     * @return a ConnectTypeResponse for the requested IP address.
     * @throws com.maxmind.geoip2.exception.GeoIp2Exception if there is an error looking up the IP
     * @throws java.io.IOException                          if there is an IO error
     */
    ConnectionTypeResponse connectionType(InetAddress ipAddress)
            throws IOException, GeoIp2Exception;
    
    /**
     * Same as {@link #connectionType(InetAddress)} but returns null when the IP is not in our database.
     * 
     * @param ipAddress
     * @return
     * @throws IOException
     * @throws GeoIp2Exception
     */
    ConnectionTypeResponse tryConnectionType(InetAddress ipAddress)
            throws IOException, GeoIp2Exception;

    /**
     * Look up an IP address in a GeoIP2 Domain database.
     *
     * @param ipAddress IPv4 or IPv6 address to lookup.
     * @return a DomainResponse for the requested IP address.
     * @throws com.maxmind.geoip2.exception.GeoIp2Exception if there is an error looking up the IP
     * @throws java.io.IOException                          if there is an IO error
     */
    DomainResponse domain(InetAddress ipAddress) throws IOException,
            GeoIp2Exception;
    
    /**
     * Same as {@link #domain(InetAddress)} but returns null when the IP is not in our database.
     * 
     * @param ipAddress
     * @return
     * @throws IOException
     * @throws GeoIp2Exception
     */
    DomainResponse tryDomain(InetAddress ipAddress) throws IOException,
            GeoIp2Exception;

    /**
     * Look up an IP address in a GeoIP2 Enterprise database.
     *
     * @param ipAddress IPv4 or IPv6 address to lookup.
     * @return an EnterpriseResponse for the requested IP address.
     * @throws com.maxmind.geoip2.exception.GeoIp2Exception if there is an error looking up the IP
     * @throws java.io.IOException                          if there is an IO error
     */
    EnterpriseResponse enterprise(InetAddress ipAddress) throws IOException,
            GeoIp2Exception;
    
    /**
     * Same as {@link #enterprise(InetAddress)} but returns null when the IP is not in our database.
     * 
     * @param ipAddress
     * @return
     * @throws IOException
     * @throws GeoIp2Exception
     */
    EnterpriseResponse tryEnterprise(InetAddress ipAddress) throws IOException,
            GeoIp2Exception;

    /**
     * Look up an IP address in a GeoIP2 ISP database.
     *
     * @param ipAddress IPv4 or IPv6 address to lookup.
     * @return an IspResponse for the requested IP address.
     * @throws com.maxmind.geoip2.exception.GeoIp2Exception if there is an error looking up the IP
     * @throws java.io.IOException                          if there is an IO error
     */
    IspResponse isp(InetAddress ipAddress) throws IOException,
            GeoIp2Exception;
    
    /**
     * Same as {@link #isp(InetAddress)} but returns null when the IP is not in our database.
     * @param ipAddress
     * @return
     * @throws IOException
     * @throws GeoIp2Exception
     */
    IspResponse tryIsp(InetAddress ipAddress) throws IOException,
            GeoIp2Exception;
}
