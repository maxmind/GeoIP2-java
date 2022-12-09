package com.maxmind.geoip2.model;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.maxmind.db.MaxMindDbConstructor;
import com.maxmind.db.MaxMindDbParameter;
import com.maxmind.db.Network;
import com.maxmind.geoip2.NetworkDeserializer;

/**
 * This class provides the GeoLite2 ASN model.
 */
public class AsnResponse extends AbstractResponse {

    private final Long autonomousSystemNumber;
    private final String autonomousSystemOrganization;
    private final String ipAddress;
    private final Network network;

    @MaxMindDbConstructor
    public AsnResponse(
        @JsonProperty("autonomous_system_number")
        @MaxMindDbParameter(name = "autonomous_system_number") Long autonomousSystemNumber,
        @JsonProperty("autonomous_system_organization")
        @MaxMindDbParameter(name = "autonomous_system_organization")
        String autonomousSystemOrganization,
        @JacksonInject("ip_address") @JsonProperty("ip_address")
        @MaxMindDbParameter(name = "ip_address") String ipAddress,
        @JacksonInject("network") @JsonProperty("network")
        @JsonDeserialize(using = NetworkDeserializer.class) @MaxMindDbParameter(name = "network")
        Network network
    ) {
        this.autonomousSystemNumber = autonomousSystemNumber;
        this.autonomousSystemOrganization = autonomousSystemOrganization;
        this.ipAddress = ipAddress;
        this.network = network;
    }

    public AsnResponse(
        AsnResponse response,
        String ipAddress,
        Network network
    ) {
        this(
            response.getAutonomousSystemNumber(),
            response.getAutonomousSystemOrganization(),
            ipAddress,
            network
        );
    }

    /**
     * @return The autonomous system number associated with the IP address.
     */
    @JsonProperty("autonomous_system_number")
    public Long getAutonomousSystemNumber() {
        return this.autonomousSystemNumber;
    }

    /**
     * @return The organization associated with the registered autonomous system
     * number for the IP address
     */
    @JsonProperty("autonomous_system_organization")
    public String getAutonomousSystemOrganization() {
        return this.autonomousSystemOrganization;
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
