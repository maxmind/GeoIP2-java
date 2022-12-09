package com.maxmind.geoip2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.maxmind.db.Network;

/**
 * This class provides the base IP model.
 */
public class IpBaseResponse extends AbstractResponse {

    private final boolean isAnonymous;
    private final boolean isAnonymousVpn;
    private final boolean isHostingProvider;
    private final boolean isPublicProxy;
    private final boolean isResidentialProxy;
    private final boolean isTorExitNode;
    private final String ipAddress;
    private final Network network;

    public IpBaseResponse(
        String ipAddress,
        boolean isAnonymous,
        boolean isAnonymousVpn,
        boolean isHostingProvider,
        boolean isPublicProxy,
        boolean isResidentialProxy,
        boolean isTorExitNode,
        Network network
    ) {
        this.isAnonymous = isAnonymous;
        this.isAnonymousVpn = isAnonymousVpn;
        this.isHostingProvider = isHostingProvider;
        this.isPublicProxy = isPublicProxy;
        this.isResidentialProxy = isResidentialProxy;
        this.isTorExitNode = isTorExitNode;
        this.ipAddress = ipAddress;
        this.network = network;
    }

    /**
     * @return whether the IP address belongs to any sort of anonymous network.
     */
    @JsonProperty("is_anonymous")
    public boolean isAnonymous() {
        return isAnonymous;
    }

    /**
     * @return whether the IP address is registered to an anonymous VPN
     * provider. If a VPN provider does not register subnets under names
     * associated with them, we will likely only flag their IP ranges using
     * isHostingProvider.
     */
    @JsonProperty("is_anonymous_vpn")
    public boolean isAnonymousVpn() {
        return isAnonymousVpn;
    }

    /**
     * @return whether the IP address belongs to a hosting or VPN provider
     * (see description of isAnonymousVpn).
     */
    @JsonProperty("is_hosting_provider")
    public boolean isHostingProvider() {
        return isHostingProvider;
    }

    /**
     * @return whether the IP address belongs to a public proxy.
     */
    @JsonProperty("is_public_proxy")
    public boolean isPublicProxy() {
        return isPublicProxy;
    }

    /**
     * @return whether the IP address is on a suspected anonymizing network and
     * belongs to a residential ISP.
     */
    @JsonProperty("is_residential_proxy")
    public boolean isResidentialProxy() {
        return isResidentialProxy;
    }

    /**
     * @return whether the IP address is a Tor exit node.
     */
    @JsonProperty("is_tor_exit_node")
    public boolean isTorExitNode() {
        return isTorExitNode;
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
