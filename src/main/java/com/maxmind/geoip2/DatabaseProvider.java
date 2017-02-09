package com.maxmind.geoip2;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;

import com.maxmind.db.Metadata;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.AnonymousIpResponse;
import com.maxmind.geoip2.model.ConnectionTypeResponse;
import com.maxmind.geoip2.model.DomainResponse;
import com.maxmind.geoip2.model.EnterpriseResponse;
import com.maxmind.geoip2.model.IspResponse;

public interface DatabaseProvider extends GeoIp2Provider, Closeable {
    /**
     * Look up an IP address in a GeoIP2 Anonymous IP.
     *
     * @param ipAddress IPv4 or IPv6 address to lookup.
     * @return a AnonymousIpResponse for the requested IP address.
     * @throws com.maxmind.geoip2.exception.GeoIp2Exception if there is an error looking up the IP
     * @throws java.io.IOException if there is an IO error
     */
    AnonymousIpResponse anonymousIp(InetAddress ipAddress) throws IOException, GeoIp2Exception;

    /**
     * Look up an IP address in a GeoIP2 Connection Type database.
     *
     * @param ipAddress IPv4 or IPv6 address to lookup.
     * @return a ConnectTypeResponse for the requested IP address.
     * @throws com.maxmind.geoip2.exception.GeoIp2Exception if there is an error looking up the IP
     * @throws java.io.IOException if there is an IO error
     */
    ConnectionTypeResponse connectionType(InetAddress ipAddress) throws IOException, GeoIp2Exception;

    /**
     * Look up an IP address in a GeoIP2 Domain database.
     *
     * @param ipAddress IPv4 or IPv6 address to lookup.
     * @return a DomainResponse for the requested IP address.
     * @throws com.maxmind.geoip2.exception.GeoIp2Exception if there is an error looking up the IP
     * @throws java.io.IOException if there is an IO error
     */
    DomainResponse domain(InetAddress ipAddress) throws IOException, GeoIp2Exception;

    /**
     * Look up an IP address in a GeoIP2 Enterprise database.
     *
     * @param ipAddress IPv4 or IPv6 address to lookup.
     * @return an EnterpriseResponse for the requested IP address.
     * @throws com.maxmind.geoip2.exception.GeoIp2Exception if there is an error looking up the IP
     * @throws java.io.IOException if there is an IO error
     */
    EnterpriseResponse enterprise(InetAddress ipAddress) throws IOException, GeoIp2Exception;

    /**
     * Look up an IP address in a GeoIP2 ISP database.
     *
     * @param ipAddress IPv4 or IPv6 address to lookup.
     * @return an IspResponse for the requested IP address.
     * @throws com.maxmind.geoip2.exception.GeoIp2Exception if there is an error looking up the IP
     * @throws java.io.IOException if there is an IO error
     */
    IspResponse isp(InetAddress ipAddress) throws IOException, GeoIp2Exception;

    /**
     * @return the metadata for the open MaxMind DB file.
     * @throws IOException
     */
    Metadata getMetadata() throws IOException;
}