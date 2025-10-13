package com.maxmind.geoip2.model;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.maxmind.db.MaxMindDbConstructor;
import com.maxmind.db.MaxMindDbParameter;
import com.maxmind.db.Network;
import com.maxmind.geoip2.JsonSerializable;
import com.maxmind.geoip2.NetworkDeserializer;

/**
 * This class provides the GeoIP2 ISP model.
 *
 * @param autonomousSystemNumber The autonomous system number associated with the IP address.
 * @param autonomousSystemOrganization The organization associated with the registered autonomous
 *                                     system number for the IP address.
 * @param ipAddress The IP address that the data in the model is for.
 * @param isp The name of the ISP associated with the IP address.
 * @param mobileCountryCode The <a href="https://en.wikipedia.org/wiki/Mobile_country_code">
 *                          mobile country code (MCC)</a> associated with the IP address and ISP.
 *                          This property is available from the City and Insights web services and
 *                          the GeoIP2 Enterprise database.
 * @param mobileNetworkCode The <a href="https://en.wikipedia.org/wiki/Mobile_country_code">
 *                          mobile network code (MNC)</a> associated with the IP address and ISP.
 *                          This property is available from the City and Insights web services and
 *                          the GeoIP2 Enterprise database.
 * @param organization The name of the organization associated with the IP address.
 * @param network The network associated with the record. In particular, this is the largest
 *                network where all the fields besides IP address have the same value.
 */
public record IspResponse(
    @JsonProperty("autonomous_system_number")
    @MaxMindDbParameter(name = "autonomous_system_number")
    Long autonomousSystemNumber,

    @JsonProperty("autonomous_system_organization")
    @MaxMindDbParameter(name = "autonomous_system_organization")
    String autonomousSystemOrganization,

    @JsonProperty("ip_address")
    @MaxMindDbParameter(name = "ip_address")
    String ipAddress,

    @JsonProperty("isp")
    @MaxMindDbParameter(name = "isp")
    String isp,

    @JsonProperty("mobile_country_code")
    @MaxMindDbParameter(name = "mobile_country_code")
    String mobileCountryCode,

    @JsonProperty("mobile_network_code")
    @MaxMindDbParameter(name = "mobile_network_code")
    String mobileNetworkCode,

    @JsonProperty("organization")
    @MaxMindDbParameter(name = "organization")
    String organization,

    @JsonProperty("network")
    @JsonDeserialize(using = NetworkDeserializer.class)
    @MaxMindDbParameter(name = "network")
    Network network
) implements JsonSerializable {

    /**
     * Canonical constructor.
     */
    @MaxMindDbConstructor
    public IspResponse {
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
            response.autonomousSystemNumber(),
            response.autonomousSystemOrganization(),
            ipAddress,
            response.isp(),
            response.mobileCountryCode(),
            response.mobileNetworkCode(),
            response.organization(),
            network
        );
    }

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
        return ipAddress();
    }

    /**
     * @return The name of the ISP associated with the IP address.
     * @deprecated Use {@link #isp()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    public String getIsp() {
        return isp();
    }

    /**
     * @return The <a href="https://en.wikipedia.org/wiki/Mobile_country_code">
     * mobile country code (MCC)</a> associated with the IP address and ISP.
     * This property is available from the City and Insights web services and
     * the GeoIP2 Enterprise database.
     * @deprecated Use {@link #mobileCountryCode()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    @JsonProperty("mobile_country_code")
    public String getMobileCountryCode() {
        return mobileCountryCode();
    }

    /**
     * @return The <a href="https://en.wikipedia.org/wiki/Mobile_country_code">
     * mobile network code (MNC)</a> associated with the IP address and ISP.
     * This property is available from the City and Insights web services and
     * the GeoIP2 Enterprise database.
     * @deprecated Use {@link #mobileNetworkCode()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    @JsonProperty("mobile_network_code")
    public String getMobileNetworkCode() {
        return mobileNetworkCode();
    }

    /**
     * @return The name of the organization associated with the IP address.
     * @deprecated Use {@link #organization()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    public String getOrganization() {
        return organization();
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
