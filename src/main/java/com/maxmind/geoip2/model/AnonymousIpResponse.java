package com.maxmind.geoip2.model;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.maxmind.db.MaxMindDbConstructor;
import com.maxmind.db.MaxMindDbParameter;
import com.maxmind.db.Network;
import com.maxmind.geoip2.NetworkDeserializer;

/**
 * This class provides the GeoIP2 Anonymous IP model.
 */
public class AnonymousIpResponse extends IpBaseResponse {

    public AnonymousIpResponse(
        @JacksonInject("ip_address") @JsonProperty("ip_address") String ipAddress,
        @JsonProperty("is_anonymous") boolean isAnonymous,
        @JsonProperty("is_anonymous_vpn") boolean isAnonymousVpn,
        @JsonProperty("is_hosting_provider") boolean isHostingProvider,
        @JsonProperty("is_public_proxy") boolean isPublicProxy,
        @JsonProperty("is_residential_proxy") boolean isResidentialProxy,
        @JsonProperty("is_tor_exit_node") boolean isTorExitNode,
        @JacksonInject("network") @JsonProperty("network")
        @JsonDeserialize(using = NetworkDeserializer.class) Network network
    ) {
        super(ipAddress, isAnonymous, isAnonymousVpn, isHostingProvider, isPublicProxy,
            isResidentialProxy, isTorExitNode, network);
    }

    @MaxMindDbConstructor
    public AnonymousIpResponse(
        @MaxMindDbParameter(name = "ip_address") String ipAddress,
        @MaxMindDbParameter(name = "is_anonymous") Boolean isAnonymous,
        @MaxMindDbParameter(name = "is_anonymous_vpn") Boolean isAnonymousVpn,
        @MaxMindDbParameter(name = "is_hosting_provider") Boolean isHostingProvider,
        @MaxMindDbParameter(name = "is_public_proxy") Boolean isPublicProxy,
        @MaxMindDbParameter(name = "is_residential_proxy") Boolean isResidentialProxy,
        @MaxMindDbParameter(name = "is_tor_exit_node") Boolean isTorExitNode,
        @MaxMindDbParameter(name = "network") Network network
    ) {
        this(
            ipAddress,
            isAnonymous != null ? isAnonymous : false,
            isAnonymousVpn != null ? isAnonymousVpn : false,
            isHostingProvider != null ? isHostingProvider : false,
            isPublicProxy != null ? isPublicProxy : false,
            isResidentialProxy != null ? isResidentialProxy : false,
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
            response.isResidentialProxy(),
            response.isTorExitNode(),
            network
        );
    }
}
