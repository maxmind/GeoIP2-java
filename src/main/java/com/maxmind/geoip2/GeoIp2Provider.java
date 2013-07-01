package com.maxmind.geoip2;

import java.io.IOException;
import java.net.InetAddress;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.City;
import com.maxmind.geoip2.model.CityIspOrg;
import com.maxmind.geoip2.model.Country;
import com.maxmind.geoip2.model.Omni;

public interface GeoIp2Provider {

    public Country country(InetAddress ipAddress) throws IOException,
            GeoIp2Exception;

    public City city(InetAddress ipAddress) throws IOException, GeoIp2Exception;

    public CityIspOrg cityIspOrg(InetAddress ipAddress) throws IOException,
            GeoIp2Exception;

    public Omni omni(InetAddress ipAddress) throws IOException, GeoIp2Exception;
}
