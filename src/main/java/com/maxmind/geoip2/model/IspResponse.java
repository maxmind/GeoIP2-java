package com.maxmind.geoip2.model;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.maxmind.db.MaxMindDbConstructor;
import com.maxmind.db.MaxMindDbParameter;
import com.maxmind.db.Network;
import com.maxmind.geoip2.NetworkDeserializer;

/**
 * This class provides the GeoIP2 ISP model.
 */
public class IspResponse extends AsnResponse {

    private final String isp;
    private final String organization;
    private final String mobileCountryCode;
    private final String mobileNetworkCode;

    /**
     * Constructs an instance of {@code IspResponse}.
     */
    @MaxMindDbConstructor
    public IspResponse(
        @JsonProperty("autonomous_system_number")
        @MaxMindDbParameter(name = "autonomous_system_number") Long autonomousSystemNumber,
        @JsonProperty("autonomous_system_organization")
        @MaxMindDbParameter(name = "autonomous_system_organization")
        String autonomousSystemOrganization,
        @JacksonInject("ip_address") @JsonProperty("ip_address")
        @MaxMindDbParameter(name = "ip_address") String ipAddress,
        @JsonProperty("isp") @MaxMindDbParameter(name = "isp") String isp,
        @JsonProperty("mobile_country_code") @MaxMindDbParameter(name = "mobile_country_code")
        String mobileCountryCode,
        @JsonProperty("mobile_network_code") @MaxMindDbParameter(name = "mobile_network_code")
        String mobileNetworkCode,
        @JsonProperty("organization") @MaxMindDbParameter(name = "organization")
        String organization,
        @JacksonInject("network") @JsonProperty("network")
        @JsonDeserialize(using = NetworkDeserializer.class) @MaxMindDbParameter(name = "network")
        Network network
    ) {
        super(autonomousSystemNumber, autonomousSystemOrganization, ipAddress, network);
        this.isp = isp;
        this.mobileCountryCode = mobileCountryCode;
        this.mobileNetworkCode = mobileNetworkCode;
        this.organization = organization;
    }

    /**
     * Constructs an instance of {@code IspResponse}. 
     *
     * @param response The {@code AsnResponse} object to copy.
     * @param ipAddress The IP address that the data in the model is for.
     * @param network The network associated with the record.
     */
    public IspResponse(
        IspResponse response,
        String ipAddress,
        Network network
    ) {
        this(
            response.getAutonomousSystemNumber(),
            response.getAutonomousSystemOrganization(),
            ipAddress,
            response.getIsp(),
            response.getMobileCountryCode(),
            response.getMobileNetworkCode(),
            response.getOrganization(),
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
     * @return The <a href="https://en.wikipedia.org/wiki/Mobile_country_code">
     * mobile country code (MCC)</a> associated with the IP address and ISP.
     * This property is available from the City and Insights web services and
     * the GeoIP2 Enterprise database.
     */
    @JsonProperty("mobile_country_code")
    public String getMobileCountryCode() {
        return this.mobileCountryCode;
    }

    /**
     * @return The <a href="https://en.wikipedia.org/wiki/Mobile_country_code">
     * mobile network code (MNC)</a> associated with the IP address and ISP.
     * This property is available from the City and Insights web services and
     * the GeoIP2 Enterprise database.
     */
    @JsonProperty("mobile_network_code")
    public String getMobileNetworkCode() {
        return this.mobileNetworkCode;
    }

    /**
     * @return The name of the organization associated with the IP address.
     */
    public String getOrganization() {
        return this.organization;
    }
}
