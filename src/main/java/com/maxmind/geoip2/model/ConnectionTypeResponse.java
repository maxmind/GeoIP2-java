package com.maxmind.geoip2.model;

import com.fasterxml.jackson.annotation.JacksonInject;
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

        ConnectionType(String name) {
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


    ConnectionTypeResponse() {
        this(null, null);
    }

    public ConnectionTypeResponse(
            @JsonProperty("connection_type") ConnectionType connectionType,
            @JacksonInject("ip_address") @JsonProperty("ip_address") String ipAddress
    ) {
        this.connectionType = connectionType;
        this.ipAddress = ipAddress;
    }

    /**
     * @return The connection type of the IP address.
     */
    @JsonProperty("connection_type")
    public ConnectionType getConnectionType() {
        return this.connectionType;
    }

    /**
     * @return The IP address that the data in the model is for.
     */
    @JsonProperty("ip_address")
    public String getIpAddress() {
        return this.ipAddress;
    }
}
