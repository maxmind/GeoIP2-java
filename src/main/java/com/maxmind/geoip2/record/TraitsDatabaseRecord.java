package com.maxmind.geoip2.record;

import com.maxmind.db.MaxMindDbConstructor;
import com.maxmind.db.MaxMindDbParameter;
import com.maxmind.geoip2.model.ConnectionTypeResponse.ConnectionType;

public final class TraitsDatabaseRecord {
    private final Integer autonomousSystemNumber;
    private final String autonomousSystemOrganization;
    private final ConnectionType connectionType;
    private final String domain;
    private final Boolean isAnonymous;
    private final Boolean isAnonymousProxy;
    private final Boolean isAnonymousVpn;
    private final Boolean isHostingProvider;
    private final Boolean isLegitimateProxy;
    private final Boolean isPublicProxy;
    private final Boolean isSatelliteProvider;
    private final Boolean isTorExitNode;
    private final String isp;
    private final String organization;
    private final String userType;
    private final Integer userCount;
    private final Double staticIpScore;

    @MaxMindDbConstructor
    public TraitsDatabaseRecord(
            @MaxMindDbParameter(name="autonomous_system_number") Integer autonomousSystemNumber,
            @MaxMindDbParameter(name="autonomous_system_organization") String autonomousSystemOrganization,
            @MaxMindDbParameter(name="connection_type") ConnectionType connectionType,
            @MaxMindDbParameter(name="domain") String domain,
            @MaxMindDbParameter(name="is_anonymous") Boolean isAnonymous,
            @MaxMindDbParameter(name="is_anonymous_proxy") Boolean isAnonymousProxy,
            @MaxMindDbParameter(name="is_anonymous_vpn") Boolean isAnonymousVpn,
            @MaxMindDbParameter(name="is_hosting_provider") Boolean isHostingProvider,
            @MaxMindDbParameter(name="is_legitimate_proxy") Boolean isLegitimateProxy,
            @MaxMindDbParameter(name="is_public_proxy") Boolean isPublicProxy,
            @MaxMindDbParameter(name="is_satellite_provider") Boolean isSatelliteProvider,
            @MaxMindDbParameter(name="is_tor_exit_node") Boolean isTorExitNode,
            @MaxMindDbParameter(name="isp") String isp,
            @MaxMindDbParameter(name="organization") String organization,
            @MaxMindDbParameter(name="static_ip_score") Double staticIpScore,
            @MaxMindDbParameter(name="user_count") Integer userCount,
            @MaxMindDbParameter(name="user_type") String userType
    ) {
        this.autonomousSystemNumber = autonomousSystemNumber;
        this.autonomousSystemOrganization = autonomousSystemOrganization;
        this.connectionType = connectionType;
        this.domain = domain;
        this.isAnonymous = isAnonymous;
        this.isAnonymousProxy = isAnonymousProxy;
        this.isAnonymousVpn = isAnonymousVpn;
        this.isHostingProvider = isHostingProvider;
        this.isLegitimateProxy = isLegitimateProxy;
        this.isPublicProxy = isPublicProxy;
        this.isSatelliteProvider = isSatelliteProvider;
        this.isTorExitNode = isTorExitNode;
        this.isp = isp;
        this.organization = organization;
        this.staticIpScore = staticIpScore;
        this.userCount = userCount;
        this.userType = userType;
    }

    public Integer getAutonomousSystemNumber() {
        return this.autonomousSystemNumber;
    }

    public String getAutonomousSystemOrganization() {
        return this.autonomousSystemOrganization;
    }

    public ConnectionType getConnectionType() {
        return this.connectionType;
    }

    public String getDomain() {
        return this.domain;
    }

    public boolean isAnonymous() {
        if (this.isAnonymous == null) {
            return false;
        }
        return this.isAnonymous;
    }

    public boolean isAnonymousProxy() {
        if (this.isAnonymousProxy == null) {
            return false;
        }
        return this.isAnonymousProxy;
    }

    public boolean isAnonymousVpn() {
        if (this.isAnonymousVpn == null) {
            return false;
        }
        return this.isAnonymousVpn;
    }

    public boolean isHostingProvider() {
        if (this.isHostingProvider == null) {
            return false;
        }
        return this.isHostingProvider;
    }

    public boolean isLegitimateProxy() {
        if (this.isLegitimateProxy == null) {
            return false;
        }
        return this.isLegitimateProxy;
    }

    public boolean isPublicProxy() {
        if (this.isPublicProxy == null) {
            return false;
        }
        return this.isPublicProxy;
    }

    public boolean isSatelliteProvider() {
        if (this.isSatelliteProvider == null) {
            return false;
        }
        return this.isSatelliteProvider;
    }

    public boolean isTorExitNode() {
        if (this.isTorExitNode == null) {
            return false;
        }
        return this.isTorExitNode;
    }

    public String getIsp() {
        return this.isp;
    }

    public String getOrganization() {
        return this.organization;
    }

    public Double getStaticIpScore() {
        return this.staticIpScore;
    }

    public Integer getUserCount() {
        return this.userCount;
    }

    public String getUserType() {
        return this.userType;
    }
}
