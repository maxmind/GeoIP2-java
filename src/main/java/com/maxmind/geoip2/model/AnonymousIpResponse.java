package com.maxmind.geoip2.model;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.maxmind.db.MaxMindDbConstructor;
import com.maxmind.db.MaxMindDbParameter;
import com.maxmind.db.Network;
import com.maxmind.geoip2.NetworkDeserializer;

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
    private final Network network;

    AnonymousIpResponse() {
        this(null, false, false, false, false, false);
    }

    // This is for compatibility and should be removed if we do a major release.
    public AnonymousIpResponse(
            String ipAddress,
            boolean isAnonymous,
            boolean isAnonymousVpn,
            boolean isHostingProvider,
            boolean isPublicProxy,
            boolean isTorExitNode
    ) {
        this(ipAddress, isAnonymous, isAnonymousVpn, isHostingProvider, isPublicProxy, isTorExitNode, null);
    }

    public AnonymousIpResponse(
            @JacksonInject("ip_address") @JsonProperty("ip_address") String ipAddress,
            @JsonProperty("is_anonymous") boolean isAnonymous,
            @JsonProperty("is_anonymous_vpn") boolean isAnonymousVpn,
            @JsonProperty("is_hosting_provider") boolean isHostingProvider,
            @JsonProperty("is_public_proxy") boolean isPublicProxy,
            @JsonProperty("is_tor_exit_node") boolean isTorExitNode,
            @JacksonInject("network") @JsonProperty("network") @JsonDeserialize(using = NetworkDeserializer.class) Network network
    ) {
        this.isAnonymous = isAnonymous;
        this.isAnonymousVpn = isAnonymousVpn;
        this.isHostingProvider = isHostingProvider;
        this.isPublicProxy = isPublicProxy;
        this.isTorExitNode = isTorExitNode;
        this.ipAddress = ipAddress;
        this.network = network;
    }

    @MaxMindDbConstructor
    public AnonymousIpResponse(
            @MaxMindDbParameter(name="ip_address") String ipAddress,
            @MaxMindDbParameter(name="is_anonymous") Boolean isAnonymous,
            @MaxMindDbParameter(name="is_anonymous_vpn") Boolean isAnonymousVpn,
            @MaxMindDbParameter(name="is_hosting_provider") Boolean isHostingProvider,
            @MaxMindDbParameter(name="is_public_proxy") Boolean isPublicProxy,
            @MaxMindDbParameter(name="is_tor_exit_node") Boolean isTorExitNode,
            @MaxMindDbParameter(name="network") Network network
    ) {
        this(
            ipAddress,
            isAnonymous != null ? isAnonymous : false,
            isAnonymousVpn != null ? isAnonymousVpn : false,
            isHostingProvider != null ? isHostingProvider : false,
            isPublicProxy != null ? isPublicProxy : false,
            isTorExitNode != null ? isTorExitNode : false,
            network
        );
    }

    public AnonymousIpResponse(
            AnonymousIpResponse response,
            String ipAddress,
            Network network
    ) {
        this(
            ipAddress,
            response.isAnonymous(),
            response.isAnonymousVpn(),
            response.isHostingProvider(),
            response.isPublicProxy(),
            response.isTorExitNode(),
            network
        );
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
     * the largest network where all of the fields besides IP address have the
     * same value.
     */
    @JsonProperty
    @JsonSerialize(using = ToStringSerializer.class)
    public Network getNetwork() {
        return this.network;
    }
}
