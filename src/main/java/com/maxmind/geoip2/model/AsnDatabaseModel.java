package com.maxmind.geoip2.model;

import com.maxmind.db.MaxMindDbConstructor;
import com.maxmind.db.MaxMindDbParameter;

public class AsnDatabaseModel {
    private final Long autonomousSystemNumber;
    private final String autonomousSystemOrganization;

    @MaxMindDbConstructor
    public AsnDatabaseModel (
        @MaxMindDbParameter(name="autonomous_system_number") Long autonomousSystemNumber,
        @MaxMindDbParameter(name="autonomous_system_organization") String autonomousSystemOrganization
    ) {
        this.autonomousSystemNumber = autonomousSystemNumber;
        this.autonomousSystemOrganization = autonomousSystemOrganization;
    }

    public Long getAutonomousSystemNumber() {
        return this.autonomousSystemNumber;
    }

    public String getAutonomousSystemOrganization() {
        return this.autonomousSystemOrganization;
    }
}
