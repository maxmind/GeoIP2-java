package com.maxmind.geoip2.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class provides the GeoIP2 Anonymous IP model.
 */
public class AnonymousIpResponse extends AbstractResponse {

    @JsonProperty("is_anonymous")
    private boolean isAnonymous;

    @JsonProperty("is_anonymous_vpn")
    private boolean isAnonymousVpn;

    @JsonProperty("is_hosting_provider")
    private boolean isHostingProvider;

    @JsonProperty("is_public_proxy")
    private boolean isPublicProxy;

    @JsonProperty("is_tor_exit_node")
    private boolean isTorExitNode;

    @JsonProperty("ip_address")
    private String ipAddress;


    public AnonymousIpResponse() {

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
