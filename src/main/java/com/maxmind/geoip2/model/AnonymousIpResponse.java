package com.maxmind.geoip2.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class provides the GeoIP2 Anonymous IP model.
 */
public class AnonymousIpResponse extends AbstractResponse {

    private final boolean isAnonymous;
    private final boolean isAnonymousVpn;
    private final boolean isHostingProvider;
    private final boolean isPublicProxy;
    private final boolean isTorExitNode;
    private final String ipAddress;

    public AnonymousIpResponse(@JsonProperty("is_anonymous") boolean isAnonymous, @JsonProperty("is_anonymous_vpn") boolean isAnonymousVpn,
                               @JsonProperty("is_hosting_provider") boolean isHostingProvider, @JsonProperty("is_public_proxy") boolean isPublicProxy,
                               @JsonProperty("is_tor_exit_node") boolean isTorExitNode, @JsonProperty("ip_address") String ipAddress) {
        this.isAnonymous = isAnonymous;
        this.isAnonymousVpn = isAnonymousVpn;
        this.isHostingProvider = isHostingProvider;
        this.isPublicProxy = isPublicProxy;
        this.isTorExitNode = isTorExitNode;
        this.ipAddress = ipAddress;
    }

    /**
     * @return whether the IP address belongs to any sort of anonymous network.
     */
    public boolean isAnonymous() {
        return isAnonymous;
    }

    /**
     * @return whether the IP address belongs to an anonymous VPN system.
     */
    public boolean isAnonymousVpn() {
        return isAnonymousVpn;
    }

    /**
     * @return whether the IP address belongs to a hosting provider.
     */
    public boolean isHostingProvider() {
        return isHostingProvider;
    }

    /**
     * @return whether the IP address belongs to a public proxy.
     */
    public boolean isPublicProxy() {
        return isPublicProxy;
    }

    /**
     * @return whether the IP address is a Tor exit node.
     */
    public boolean isTorExitNode() {
        return isTorExitNode;
    }


    /**
     * @return The IP address that the data in the model is for.
     */
    public String getIpAddress() {
        return this.ipAddress;
    }

    @Override
    public String toString() {
        return "AnonymousIpResponse[" +
                "isAnonymous=" + isAnonymous +
                ", isAnonymousVpn=" + isAnonymousVpn +
                ", isHostingProvider=" + isHostingProvider +
                ", isPublicProxy=" + isPublicProxy +
                ", isTorExitNode=" + isTorExitNode +
                ", ipAddress='" + ipAddress + '\'' +
                ']';
    }
}
