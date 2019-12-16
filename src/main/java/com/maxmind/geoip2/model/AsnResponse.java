package com.maxmind.geoip2.model;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.maxmind.db.Network;
import com.maxmind.geoip2.NetworkDeserializer;

/**
 * This class provides the GeoLite2 ASN model.
 */
public class AsnResponse extends AbstractResponse {

    private final Integer autonomousSystemNumber;
    private final String autonomousSystemOrganization;
    private final String ipAddress;
    private final Network network;

    AsnResponse() {
        this(null, null, null);
    }

    public AsnResponse(
            Integer autonomousSystemNumber,
            String autonomousSystemOrganization,
            String ipAddress
    ) {
        this(autonomousSystemNumber, autonomousSystemOrganization, ipAddress, null);
    }

    public AsnResponse(
            @JsonProperty("autonomous_system_number") Integer autonomousSystemNumber,
            @JsonProperty("autonomous_system_organization") String autonomousSystemOrganization,
            @JacksonInject("ip_address") @JsonProperty("ip_address") String ipAddress,
            @JacksonInject("network") @JsonProperty("network") @JsonDeserialize(using = NetworkDeserializer.class) Network network
    ) {
        this.autonomousSystemNumber = autonomousSystemNumber;
        this.autonomousSystemOrganization = autonomousSystemOrganization;
        this.ipAddress = ipAddress;
        this.network = network;
    }

    /**
     * @return The autonomous system number associated with the IP address.
     */
    @JsonProperty("autonomous_system_number")
    public Integer getAutonomousSystemNumber() {
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
     * the largest network where all of the fields besides IP address have the
     * same value.
     */
    @JsonProperty
    @JsonSerialize(using = ToStringSerializer.class)
    public Network getNetwork() {
        return this.network;
    }
}
