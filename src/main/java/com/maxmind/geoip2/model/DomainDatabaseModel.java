package com.maxmind.geoip2.model;

import com.maxmind.db.MaxMindDbConstructor;
import com.maxmind.db.MaxMindDbParameter;

public class DomainDatabaseModel {
    private final String domain;

    @MaxMindDbConstructor
    public DomainDatabaseModel (
        @MaxMindDbParameter(name="domain") String domain
    ) {
        this.domain = domain;
    }

    public String getDomain() {
        return this.domain;
    }
}
