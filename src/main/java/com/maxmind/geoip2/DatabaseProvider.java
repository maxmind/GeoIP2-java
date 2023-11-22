package com.maxmind.geoip2;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.AnonymousIpResponse;
import com.maxmind.geoip2.model.AsnResponse;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.ConnectionTypeResponse;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.model.DomainResponse;
import com.maxmind.geoip2.model.EnterpriseResponse;
import com.maxmind.geoip2.model.IpRiskResponse;
import com.maxmind.geoip2.model.IspResponse;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Optional;

public interface DatabaseProvider extends GeoIp2Provider {

    /**
     * @param ipAddress IPv4 or IPv6 address to lookup.
     * @return A Country model for the requested IP address or empty if it is not in the DB.
     * @throws GeoIp2Exception if there is an error looking up the IP
     * @throws IOException     if there is an IO error
     */
    Optional<CountryResponse> tryCountry(InetAddress ipAddress) throws IOException,
        GeoIp2Exception;

    /**
     * @param ipAddress IPv4 or IPv6 address to lookup.
     * @return A City model for the requested IP address or empty if it is not in the DB.
     * @throws GeoIp2Exception if there is an error looking up the IP
     * @throws IOException     if there is an IO error
     */
    Optional<CityResponse> tryCity(InetAddress ipAddress) throws IOException,
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
     * Look up an IP address in a GeoIP2 Anonymous IP.
     *
     * @param ipAddress IPv4 or IPv6 address to lookup.
     * @return a AnonymousIpResponse for the requested IP address or empty if it is not in the DB.
     * @throws com.maxmind.geoip2.exception.GeoIp2Exception if there is an error looking up the IP
     * @throws java.io.IOException                          if there is an IO error
     */
    Optional<AnonymousIpResponse> tryAnonymousIp(InetAddress ipAddress) throws IOException,
        GeoIp2Exception;


    /**
     * Look up an IP address in a GeoIP2 IP Risk database.
     *
     * @param ipAddress IPv4 or IPv6 address to lookup.
     * @return an IpRiskResponse for the requested IP address.
     * @throws com.maxmind.geoip2.exception.GeoIp2Exception if there is an error looking up the IP
     * @throws java.io.IOException                          if there is an IO error
     * @deprecated This database has been discontinued.
     */
    @Deprecated
    IpRiskResponse ipRisk(InetAddress ipAddress) throws IOException,
        GeoIp2Exception;

    /**
     * Look up an IP address in a GeoIP2 IP Risk database.
     *
     * @param ipAddress IPv4 or IPv6 address to lookup.
     * @return an IPRiskResponse for the requested IP address or empty if it is not in the DB.
     * @throws com.maxmind.geoip2.exception.GeoIp2Exception if there is an error looking up the IP
     * @throws java.io.IOException                          if there is an IO error
     * @deprecated This database has been discontinued.
     */
    @Deprecated
    Optional<IpRiskResponse> tryIpRisk(InetAddress ipAddress) throws IOException,
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
     * Look up an IP address in a GeoLite2 ASN database.
     *
     * @param ipAddress IPv4 or IPv6 address to lookup.
     * @return an IspResponse for the requested IP address or empty if it is not in the DB.
     * @throws com.maxmind.geoip2.exception.GeoIp2Exception if there is an error looking up the IP
     * @throws java.io.IOException                          if there is an IO error
     */
    Optional<AsnResponse> tryAsn(InetAddress ipAddress) throws IOException,
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
     * Look up an IP address in a GeoIP2 Connection Type database.
     *
     * @param ipAddress IPv4 or IPv6 address to lookup.
     * @return a ConnectTypeResponse for the requested IP address or empty if it is not in the DB.
     * @throws com.maxmind.geoip2.exception.GeoIp2Exception if there is an error looking up the IP
     * @throws java.io.IOException                          if there is an IO error
     */
    Optional<ConnectionTypeResponse> tryConnectionType(InetAddress ipAddress)
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
     * Look up an IP address in a GeoIP2 Domain database.
     *
     * @param ipAddress IPv4 or IPv6 address to lookup.
     * @return a DomainResponse for the requested IP address or empty if it is not in the DB.
     * @throws com.maxmind.geoip2.exception.GeoIp2Exception if there is an error looking up the IP
     * @throws java.io.IOException                          if there is an IO error
     */
    Optional<DomainResponse> tryDomain(InetAddress ipAddress) throws IOException,
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
     * Look up an IP address in a GeoIP2 Enterprise database.
     *
     * @param ipAddress IPv4 or IPv6 address to lookup.
     * @return an EnterpriseResponse for the requested IP address or empty if it is not in the DB.
     * @throws com.maxmind.geoip2.exception.GeoIp2Exception if there is an error looking up the IP
     * @throws java.io.IOException                          if there is an IO error
     */
    Optional<EnterpriseResponse> tryEnterprise(InetAddress ipAddress) throws IOException,
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
     * Look up an IP address in a GeoIP2 ISP database.
     *
     * @param ipAddress IPv4 or IPv6 address to look up or empty if it is not in the DB.
     * @return an IspResponse for the requested IP address.
     * @throws com.maxmind.geoip2.exception.GeoIp2Exception if there is an error looking up the IP
     * @throws java.io.IOException                          if there is an IO error
     */
    Optional<IspResponse> tryIsp(InetAddress ipAddress) throws IOException,
        GeoIp2Exception;
}
