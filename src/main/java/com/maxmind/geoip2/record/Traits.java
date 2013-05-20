package com.maxmind.geoip2.record;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Traits {
    @JsonProperty("autonomous_system_number")
    private Integer autonomousSystemNumber;

    @JsonProperty("autonomous_system_organization")
    private String autonomousSystemOrganization;

    @JsonProperty
    private String domain;

    @JsonProperty("ip_address")
    private String ipAddress;

    @JsonProperty("is_anonymous_proxy")
    private boolean anonymousProxy;

    @JsonProperty("is_satellite_provider")
    private boolean satelliteProvider;

    @JsonProperty
    private String isp;

    @JsonProperty
    private String organization;

    @JsonProperty("user_type")
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

    @Override
    public String toString() {
        return "Traits ["
                + (this.autonomousSystemNumber != null ? "autonomousSystemNumber="
                        + this.autonomousSystemNumber + ", "
                        : "")
                + (this.autonomousSystemOrganization != null ? "autonomousSystemOrganization="
                        + this.autonomousSystemOrganization + ", "
                        : "")
                + (this.domain != null ? "domain=" + this.domain + ", " : "")
                + (this.ipAddress != null ? "ipAddress=" + this.ipAddress
                        + ", " : "")
                + "anonymousProxy="
                + this.anonymousProxy
                + ", satelliteProvider="
                + this.satelliteProvider
                + ", "
                + (this.isp != null ? "isp=" + this.isp + ", " : "")
                + (this.organization != null ? "organization="
                        + this.organization + ", " : "")
                + (this.userType != null ? "userType=" + this.userType : "")
                + "]";
    }
}
