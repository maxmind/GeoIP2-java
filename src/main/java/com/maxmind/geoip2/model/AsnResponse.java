package com.maxmind.geoip2.model;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.maxmind.db.MaxMindDbIpAddress;
import com.maxmind.db.MaxMindDbNetwork;
import com.maxmind.db.MaxMindDbParameter;
import com.maxmind.db.Network;
import com.maxmind.geoip2.JsonSerializable;
import com.maxmind.geoip2.NetworkDeserializer;
import java.net.InetAddress;

/**
 * This class provides the GeoLite2 ASN model.
 *
 * @param autonomousSystemNumber The autonomous system number associated with the IP address.
 * @param autonomousSystemOrganization The organization associated with the registered autonomous
 *                                     system number for the IP address.
 * @param ipAddress The IP address that the data in the model is for.
 * @param network The network associated with the record. In particular, this is the largest
 *                network where all the fields besides IP address have the same value.
 */
public record AsnResponse(
    @JsonProperty("autonomous_system_number")
    @MaxMindDbParameter(name = "autonomous_system_number")
    Long autonomousSystemNumber,

    @JsonProperty("autonomous_system_organization")
    @MaxMindDbParameter(name = "autonomous_system_organization")
    String autonomousSystemOrganization,

    @JsonProperty("ip_address")
    @MaxMindDbIpAddress
    InetAddress ipAddress,

    @JsonProperty("network")
    @JsonDeserialize(using = NetworkDeserializer.class)
    @MaxMindDbNetwork
    Network network
) implements JsonSerializable {

    /**
     * @return The autonomous system number associated with the IP address.
     * @deprecated Use {@link #autonomousSystemNumber()} instead. This method will be removed
     *     in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    @JsonProperty("autonomous_system_number")
    public Long getAutonomousSystemNumber() {
        return autonomousSystemNumber();
    }

    /**
     * @return The organization associated with the registered autonomous system
     * number for the IP address
     * @deprecated Use {@link #autonomousSystemOrganization()} instead. This method will be
     *     removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    @JsonProperty("autonomous_system_organization")
    public String getAutonomousSystemOrganization() {
        return autonomousSystemOrganization();
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
