package com.maxmind.geoip2.model;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class provides the GeoLite2 ASN model.
 */
public class AsnResponse extends AbstractResponse {

    private final Integer autonomousSystemNumber;
    private final String autonomousSystemOrganization;
    private final String ipAddress;

    AsnResponse() {
        this(null, null, null);
    }

    public AsnResponse(
            @JsonProperty("autonomous_system_number") Integer autonomousSystemNumber,
            @JsonProperty("autonomous_system_organization") String autonomousSystemOrganization,
            @JacksonInject("ip_address") @JsonProperty("ip_address") String ipAddress
    ) {
        this.autonomousSystemNumber = autonomousSystemNumber;
        this.autonomousSystemOrganization = autonomousSystemOrganization;
        this.ipAddress = ipAddress;
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
}
