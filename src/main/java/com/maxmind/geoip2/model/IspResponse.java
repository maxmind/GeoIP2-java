package com.maxmind.geoip2.model;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.maxmind.db.Network;
import com.maxmind.geoip2.NetworkDeserializer;

/**
 * This class provides the GeoIP2 ISP model.
 */
public class IspResponse extends AsnResponse {

    private final String isp;
    private final String organization;

    IspResponse() {
        this(null, null, null, null, null);
    }

    public IspResponse(
            Integer autonomousSystemNumber,
            String autonomousSystemOrganization,
            String ipAddress,
            String isp,
            String organization
    ) {
        this(autonomousSystemNumber, autonomousSystemOrganization, ipAddress, isp, organization, null);
    }

    public IspResponse(
            @JsonProperty("autonomous_system_number") Integer autonomousSystemNumber,
            @JsonProperty("autonomous_system_organization") String autonomousSystemOrganization,
            @JacksonInject("ip_address") @JsonProperty("ip_address") String ipAddress,
            @JsonProperty("isp") String isp,
            @JsonProperty("organization") String organization,
            @JacksonInject("network") @JsonProperty("network") @JsonDeserialize(using = NetworkDeserializer.class) Network network
    ) {
        super(autonomousSystemNumber, autonomousSystemOrganization, ipAddress, network);
        this.isp = isp;
        this.organization = organization;
    }

    public IspResponse(
            IspDatabaseModel model,
            String ipAddress,
            Network network
    ) {
        this(
            model.getAutonomousSystemNumber() != null ? model.getAutonomousSystemNumber().intValue() : null,
            model.getAutonomousSystemOrganization(),
            ipAddress,
            model.getIsp(),
            model.getOrganization(),
            network
        );
    }

    /**
     * @return The name of the ISP associated with the IP address.
     */
    public String getIsp() {
        return this.isp;
    }

    /**
     * @return The name of the organization associated with the IP address.
     */
    public String getOrganization() {
        return this.organization;
    }
}
