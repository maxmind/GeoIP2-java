package com.maxmind.geoip2.model;

import com.maxmind.db.MaxMindDbConstructor;
import com.maxmind.db.MaxMindDbParameter;
import com.maxmind.geoip2.model.ConnectionTypeResponse.ConnectionType;

public class ConnectionTypeDatabaseModel {
    private final String connectionType;

    @MaxMindDbConstructor
    public ConnectionTypeDatabaseModel (
        @MaxMindDbParameter(name="connection_type") String connectionType
    ) {
        this.connectionType = connectionType;
    }

    public ConnectionType getConnectionType() {
        return ConnectionType.fromString(this.connectionType);
    }
}
