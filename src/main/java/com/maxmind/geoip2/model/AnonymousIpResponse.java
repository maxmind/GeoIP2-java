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

/**
 * This class provides the GeoIP2 Anonymous IP model.
 *
 * @param ipAddress The IP address that the data in the model is for.
 * @param isAnonymous Whether the IP address belongs to any sort of anonymous network.
 * @param isAnonymousVpn Whether the IP address is registered to an anonymous VPN provider. If a
 *                       VPN provider does not register subnets under names associated with them,
 *                       we will likely only flag their IP ranges using isHostingProvider.
 * @param isHostingProvider Whether the IP address belongs to a hosting or VPN provider (see
 *                          description of isAnonymousVpn).
 * @param isPublicProxy Whether the IP address belongs to a public proxy.
 * @param isResidentialProxy Whether the IP address is on a suspected anonymizing network and
 *                           belongs to a residential ISP.
 * @param isTorExitNode Whether the IP address is a Tor exit node.
 * @param network The network associated with the record. In particular, this is the largest
 *                network where all the fields besides IP address have the same value.
 */
public record AnonymousIpResponse(
    @JsonProperty("ip_address")
    @MaxMindDbIpAddress
    String ipAddress,

    @JsonProperty("is_anonymous")
    @MaxMindDbParameter(name = "is_anonymous", useDefault = true)
    boolean isAnonymous,

    @JsonProperty("is_anonymous_vpn")
    @MaxMindDbParameter(name = "is_anonymous_vpn", useDefault = true)
    boolean isAnonymousVpn,

    @JsonProperty("is_hosting_provider")
    @MaxMindDbParameter(name = "is_hosting_provider", useDefault = true)
    boolean isHostingProvider,

    @JsonProperty("is_public_proxy")
    @MaxMindDbParameter(name = "is_public_proxy", useDefault = true)
    boolean isPublicProxy,

    @JsonProperty("is_residential_proxy")
    @MaxMindDbParameter(name = "is_residential_proxy", useDefault = true)
    boolean isResidentialProxy,

    @JsonProperty("is_tor_exit_node")
    @MaxMindDbParameter(name = "is_tor_exit_node", useDefault = true)
    boolean isTorExitNode,

    @JsonProperty("network")
    @JsonDeserialize(using = NetworkDeserializer.class)
    @MaxMindDbNetwork
    Network network
) implements JsonSerializable {

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
