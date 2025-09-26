package com.maxmind.geoip2.model;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.maxmind.db.MaxMindDbConstructor;
import com.maxmind.db.MaxMindDbParameter;
import com.maxmind.db.Network;
import com.maxmind.geoip2.NetworkDeserializer;

/**
 * This class provides the GeoIP2 Connection-Type model.
 */
public class ConnectionTypeResponse extends AbstractResponse {

    /**
     * The enumerated values that connection-type may take.
     */
    public enum ConnectionType {
        DIALUP("Dialup"),
        CABLE_DSL("Cable/DSL"),
        CORPORATE("Corporate"),
        CELLULAR("Cellular"),
        SATELLITE("Satellite");

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

        /**
         * Creates an instance of {@code ConnectionTypeResponse} from a string.
         *
         * @param s The string to create the instance from.
         */
        @JsonCreator
        public static ConnectionType fromString(String s) {
            if (s == null) {
                return null;
            }

            return switch (s) {
                case "Dialup" -> ConnectionType.DIALUP;
                case "Cable/DSL" -> ConnectionType.CABLE_DSL;
                case "Corporate" -> ConnectionType.CORPORATE;
                case "Cellular" -> ConnectionType.CELLULAR;
                case "Satellite" -> ConnectionType.SATELLITE;
                default -> null;
            };
        }
    }

    private final ConnectionType connectionType;
    private final String ipAddress;
    private final Network network;

    /**
     * Constructs an instance of {@code ConnectionTypeResponse}.
     *
     * @param connectionType The connection type of the IP address.
     * @param ipAddress The IP address that the data in the model is for.
     * @param network The network associated with the record.
     */
    public ConnectionTypeResponse(
        @JsonProperty("connection_type") ConnectionType connectionType,
        @JsonProperty("ip_address") String ipAddress,
        @JsonProperty("network")
        @JsonDeserialize(using = NetworkDeserializer.class) Network network
    ) {
        this.connectionType = connectionType;
        this.ipAddress = ipAddress;
        this.network = network;
    }

    /**
     * Constructs an instance of {@code ConnectionTypeResponse}.
     *
     * @param connectionType The connection type of the IP address.
     * @param ipAddress The IP address that the data in the model is for.
     * @param network The network associated with the record.   
     */
    @MaxMindDbConstructor
    public ConnectionTypeResponse(
        @MaxMindDbParameter(name = "connection_type") String connectionType,
        @MaxMindDbParameter(name = "ip_address") String ipAddress,
        @MaxMindDbParameter(name = "network") Network network
    ) {
        this(
            ConnectionType.fromString(connectionType),
            ipAddress,
            network
        );
    }

    /**
     * Constructs an instance of {@code ConnectionTypeResponse}.
     *
     * @param response The {@code ConnectionTypeResponse} object to copy.
     * @param ipAddress The IP address that the data in the model is for.
     * @param network The network associated with the record. 
     */
    public ConnectionTypeResponse(
        ConnectionTypeResponse response,
        String ipAddress,
        Network network
    ) {
        this(
            response.getConnectionType(),
            ipAddress,
            network
        );
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

    /**
     * @return The network associated with the record. In particular, this is
     * the largest network where all the fields besides IP address have the
     * same value.
     */
    @JsonProperty
    @JsonSerialize(using = ToStringSerializer.class)
    public Network getNetwork() {
        return this.network;
    }
}
