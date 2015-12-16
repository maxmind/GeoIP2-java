package com.maxmind.geoip2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * This class provides the GeoIP2 Connection-Type model.
 */
public class ConnectionTypeResponse extends AbstractResponse {

    /**
     * The enumerated values that connection-type may take.
     */
    public enum ConnectionType {
        DIALUP("Dialup"), CABLE_DSL("Cable/DSL"), CORPORATE("Corporate"), CELLULAR(
                "Cellular");

        private final String name;

        private ConnectionType(String name) {
            this.name = name;
        }

        /*
         * (non-Javadoc)
         *
         * @see java.lang.Enum#toString()
         */
        @JsonValue
        @Override
        public String toString() {
            return this.name;
        }
    }

    private final ConnectionType connectionType;
    private final String ipAddress;

    public ConnectionTypeResponse(@JsonProperty("connection_type") ConnectionType connectionType, @JsonProperty("ip_address") String ipAddress) {
        this.connectionType = connectionType;
        this.ipAddress = ipAddress;
    }

    /**
     * @return The connection type of the IP address.
     */
    public ConnectionType getConnectionType() {
        return this.connectionType;
    }

    /**
     * @return The IP address that the data in the model is for.
     */
    public String getIpAddress() {
        return this.ipAddress;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ConnectionTypeResponse [connectionType=" + this.connectionType
                + ", ipAddress=" + this.ipAddress + "]";
    }
}
