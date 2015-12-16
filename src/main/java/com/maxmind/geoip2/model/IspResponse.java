package com.maxmind.geoip2.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class provides the GeoIP2 ISP model.
 */
public class IspResponse extends AbstractResponse {

    private final Integer autonomousSystemNumber;
    private final String autonomousSystemOrganization;
    private final String isp;
    private final String organization;
    private final String ipAddress;

    public IspResponse(@JsonProperty("autonomous_system_number") Integer autonomousSystemNumber,
                       @JsonProperty("autonomous_system_organization") String autonomousSystemOrganization,
                       @JsonProperty("isp") String isp, @JsonProperty("organization") String organization,
                       @JsonProperty("ip_address") String ipAddress) {
        this.autonomousSystemNumber = autonomousSystemNumber;
        this.autonomousSystemOrganization = autonomousSystemOrganization;
        this.isp = isp;
        this.organization = organization;
        this.ipAddress = ipAddress;
    }

    /**
     * @return The autonomous system number associated with the IP address.
     */
    public Integer getAutonomousSystemNumber() {
        return this.autonomousSystemNumber;
    }

    /**
     * @return The organization associated with the registered autonomous system
     * number for the IP address
     */
    public String getAutonomousSystemOrganization() {
        return this.autonomousSystemOrganization;
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
        return "IspResponse [autonomousSystemNumber="
                + this.autonomousSystemNumber
                + ", autonomousSystemOrganization="
                + this.autonomousSystemOrganization + ", isp=" + this.isp
                + ", organization=" + this.organization + ", ipAddress="
                + this.ipAddress + "]";
    }
}
