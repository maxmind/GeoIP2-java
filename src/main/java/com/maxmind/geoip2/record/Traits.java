package com.maxmind.geoip2.record;

import com.google.api.client.util.Key;

public class Traits {
    @Key("autonomous_system_number")
    private Integer autonomousSystemNumber;

    @Key("autonomous_system_organization")
    private String autonomousSystemOrganization;

    @Key
    private String domain;

    @Key("ip_address")
    private String ipAddress;

    @Key("is_anonymous_proxy")
    private boolean anonymousProxy;

    @Key("is_satellite_provider")
    private boolean satelliteProvider;

    @Key
    private String isp;

    @Key
    private String organization;

    @Key("user_type")
    private String userType;

    public Traits() {
        // Empty traits object
    }

    public Integer getAutonomousSystemNumber() {
        return this.autonomousSystemNumber;
    }

    public String getAutonomousSystemOrganization() {
        return this.autonomousSystemOrganization;
    }

    public String getDomain() {
        return this.domain;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public String getIsp() {
        return this.isp;
    }

    public String getOrganization() {
        return this.organization;
    }

    public String getUserType() {
        return this.userType;
    }

    public boolean isAnonymousProxy() {
        return this.anonymousProxy;
    }

    public boolean isSatelliteProvider() {
        return this.satelliteProvider;
    }
}
