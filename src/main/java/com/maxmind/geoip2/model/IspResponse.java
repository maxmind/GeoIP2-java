package com.maxmind.geoip2.model;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;

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
            @JsonProperty("autonomous_system_number") Integer autonomousSystemNumber,
            @JsonProperty("autonomous_system_organization") String autonomousSystemOrganization,
            @JacksonInject("ip_address") @JsonProperty("ip_address") String ipAddress,
            @JsonProperty("isp") String isp,
            @JsonProperty("organization") String organization
    ) {
        super(autonomousSystemNumber, autonomousSystemOrganization, ipAddress);
        this.isp = isp;
        this.organization = organization;
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
