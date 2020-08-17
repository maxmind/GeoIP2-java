package com.maxmind.geoip2.model;

import com.maxmind.db.MaxMindDbConstructor;
import com.maxmind.db.MaxMindDbParameter;

public class IspDatabaseModel {
    private final Long autonomousSystemNumber;
    private final String autonomousSystemOrganization;
    private final String isp;
    private final String organization;

    @MaxMindDbConstructor
    public IspDatabaseModel (
        @MaxMindDbParameter(name="autonomous_system_number") Long autonomousSystemNumber,
        @MaxMindDbParameter(name="autonomous_system_organization") String autonomousSystemOrganization,
        @MaxMindDbParameter(name="isp") String isp,
        @MaxMindDbParameter(name="organization") String organization
    ) {
        this.autonomousSystemNumber = autonomousSystemNumber;
        this.autonomousSystemOrganization = autonomousSystemOrganization;
        this.isp = isp;
        this.organization = organization;
    }

    public Long getAutonomousSystemNumber() {
        return this.autonomousSystemNumber;
    }

    public String getAutonomousSystemOrganization() {
        return this.autonomousSystemOrganization;
    }

    public String getIsp() {
        return this.isp;
    }

    public String getOrganization() {
        return this.organization;
    }
}
