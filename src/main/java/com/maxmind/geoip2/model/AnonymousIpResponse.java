package com.maxmind.geoip2.model;

import com.fasterxml.jackson.annotation.JacksonInject;
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

    AnonymousIpResponse() {
        this(null, false, false, false, false, false);
    }

    public AnonymousIpResponse(
            @JacksonInject("ip_address") @JsonProperty("ip_address") String ipAddress,
            @JsonProperty("is_anonymous") boolean isAnonymous,
            @JsonProperty("is_anonymous_vpn") boolean isAnonymousVpn,
            @JsonProperty("is_hosting_provider") boolean isHostingProvider,
            @JsonProperty("is_public_proxy") boolean isPublicProxy,
            @JsonProperty("is_tor_exit_node") boolean isTorExitNode
    ) {
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
    @JsonProperty("is_anonymous")
    public boolean isAnonymous() {
        return isAnonymous;
    }

    /**
     * @return whether the IP address belongs to an anonymous VPN system.
     */
    @JsonProperty("is_anonymous_vpn")
    public boolean isAnonymousVpn() {
        return isAnonymousVpn;
    }

    /**
     * @return whether the IP address belongs to a hosting provider.
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
}
