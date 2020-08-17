package com.maxmind.geoip2.model;

import com.maxmind.db.MaxMindDbConstructor;
import com.maxmind.db.MaxMindDbParameter;

public class ConnectionTypeDatabaseModel {
    private final String connectionType;

    @MaxMindDbConstructor
    public ConnectionTypeDatabaseModel (
        @MaxMindDbParameter(name="connection_type") String connectionType
    ) {
        this.connectionType = connectionType;
    }

    public ConnectionTypeResponse.ConnectionType getConnectionType() {
        if (this.connectionType == null) {
            return null;
        }

        switch (this.connectionType) {
            case "Dialup":
                return ConnectionTypeResponse.ConnectionType.DIALUP;
            case "Cable/DSL":
                return ConnectionTypeResponse.ConnectionType.CABLE_DSL;
            case "Corporate":
                return ConnectionTypeResponse.ConnectionType.CORPORATE;
            case "Cellular":
                return ConnectionTypeResponse.ConnectionType.CELLULAR;
            default:
                return ConnectionTypeResponse.ConnectionType.valueOf(this.connectionType);
        }
    }
}
