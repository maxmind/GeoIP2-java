package com.maxmind.geoip2.record;

import com.google.api.client.util.Key;

public class Traits {
    @Key("ip_address")
    private String ipAddress;
    @Key("autonomous_system_number")
    private Integer autonomousSystemNumber;
    @Key("autonomous_system_organization")
    private String autonomousSystemOrganization;
    @Key
    private String domain;
    @Key
    private String isp;
    @Key
    private String organization;
    @Key("user_type")
    private String userType;
    @Key("is_anonymous_proxy")
    private boolean anonymousProxy;

    public Traits() {
        // Empty traits object
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public Integer getAutonomousSystemNumber() {
        return autonomousSystemNumber;
    }

    public String getAutonomousSystemOrganization() {
        return autonomousSystemOrganization;
    }

    public String getDomain() {
        return domain;
    }

    public String getIsp() {
        return isp;
    }

    public String getOrganization() {
        return organization;
    }

    public String getUserType() {
        return userType;
    }

    public boolean isAnonymousProxy() {
        return anonymousProxy;
    }

}
