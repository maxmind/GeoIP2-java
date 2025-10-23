package com.maxmind.geoip2.model;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.maxmind.db.MaxMindDbCreator;
import com.maxmind.db.MaxMindDbIpAddress;
import com.maxmind.db.MaxMindDbNetwork;
import com.maxmind.db.MaxMindDbParameter;
import com.maxmind.db.Network;
import com.maxmind.geoip2.JsonSerializable;
import com.maxmind.geoip2.NetworkDeserializer;
import java.net.InetAddress;

/**
 * This class provides the GeoIP2 Connection-Type model.
 *
 * @param connectionType The connection type of the IP address.
 * @param ipAddress The IP address that the data in the model is for.
 * @param network The network associated with the record. In particular, this is the largest
 *                network where all the fields besides IP address have the same value.
 */
public record ConnectionTypeResponse(
    @JsonProperty("connection_type")
    @MaxMindDbParameter(name = "connection_type")
    ConnectionType connectionType,

    @JsonProperty("ip_address")
    @MaxMindDbIpAddress
    InetAddress ipAddress,

    @JsonProperty("network")
    @JsonDeserialize(using = NetworkDeserializer.class)
    @MaxMindDbNetwork
    Network network
) implements JsonSerializable {

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
        @MaxMindDbCreator
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

    /**
     * @return The connection type of the IP address.
     * @deprecated Use {@link #connectionType()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    @JsonProperty("connection_type")
    public ConnectionType getConnectionType() {
        return connectionType();
    }

    /**
     * @return The IP address that the data in the model is for.
     * @deprecated Use {@link #ipAddress()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    @JsonProperty("ip_address")
    public String getIpAddress() {
        return ipAddress().getHostAddress();
    }

    /**
     * @return The network associated with the record. In particular, this is
     * the largest network where all the fields besides IP address have the
     * same value.
     * @deprecated Use {@link #network()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    @JsonProperty
    @JsonSerialize(using = ToStringSerializer.class)
    public Network getNetwork() {
        return network();
    }
}
