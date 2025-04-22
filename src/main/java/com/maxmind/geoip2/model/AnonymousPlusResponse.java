package com.maxmind.geoip2.model;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.maxmind.db.MaxMindDbConstructor;
import com.maxmind.db.MaxMindDbParameter;
import com.maxmind.db.Network;
import com.maxmind.geoip2.NetworkDeserializer;
import java.time.LocalDate;

/**
 * This class provides the GeoIP Anonymous Plus model.
 */
public class AnonymousPlusResponse extends AnonymousIpResponse {
    private final Integer anonymizerConfidence;
    private final LocalDate networkLastSeen;
    private final String providerName;

    /**
     * Constructs an instance of {@code AnonymousPlusResponse} with the specified values.
     *
     * @param anonymizerConfidence confidence that the network is a VPN.
     * @param ipAddress            the IP address being checked
     * @param isAnonymous          whether the IP address belongs to any sort of anonymous network
     * @param isAnonymousVpn       whether the IP address belongs to an anonymous VPN system
     * @param isHostingProvider    whether the IP address belongs to a hosting provider
     * @param isPublicProxy        whether the IP address belongs to a public proxy system
     * @param isResidentialProxy   whether the IP address belongs to a residential proxy system
     * @param isTorExitNode        whether the IP address is a Tor exit node
     * @param network              the network associated with the record
     * @param networkLastSeen      the last sighting of the network.
     * @param providerName         the name of the VPN provider.
     */
    public AnonymousPlusResponse(
        @JsonProperty("anonymizer_confidence") Integer anonymizerConfidence,
        @JacksonInject("ip_address") @JsonProperty("ip_address") String ipAddress,
        @JsonProperty("is_anonymous") Boolean isAnonymous,
        @JsonProperty("is_anonymous_vpn") Boolean isAnonymousVpn,
        @JsonProperty("is_hosting_provider") Boolean isHostingProvider,
        @JsonProperty("is_public_proxy") Boolean isPublicProxy,
        @JsonProperty("is_residential_proxy") Boolean isResidentialProxy,
        @JsonProperty("is_tor_exit_node") Boolean isTorExitNode,
        @JacksonInject("network") @JsonDeserialize(using = NetworkDeserializer.class)
        @JsonProperty("network") Network network,
        @JsonProperty("network_last_seen") LocalDate networkLastSeen,
        @JsonProperty("provider_name") String providerName
    ) {
        super(ipAddress, isAnonymous, isAnonymousVpn, isHostingProvider, isPublicProxy,
            isResidentialProxy, isTorExitNode, network);

        this.anonymizerConfidence = anonymizerConfidence;
        this.networkLastSeen = networkLastSeen;
        this.providerName = providerName;
    }

    /**
     * Constructs an instance of {@code AnonymousPlusResponse} with the specified values.
     *
     * @param anonymizerConfidence confidence that the network is a VPN.
     * @param ipAddress            the IP address being checked
     * @param isAnonymous          whether the IP address belongs to any sort of anonymous network
     * @param isAnonymousVpn       whether the IP address belongs to an anonymous VPN system
     * @param isHostingProvider    whether the IP address belongs to a hosting provider
     * @param isPublicProxy        whether the IP address belongs to a public proxy system
     * @param isResidentialProxy   whether the IP address belongs to a residential proxy system
     * @param isTorExitNode        whether the IP address is a Tor exit node
     * @param network              the network associated with the record
     * @param networkLastSeen      the last sighting of the network.
     * @param providerName         the name of the VPN provider.
     */
    @MaxMindDbConstructor
    public AnonymousPlusResponse(
        @MaxMindDbParameter(name = "anonymizer_confidence") Integer anonymizerConfidence,
        @MaxMindDbParameter(name = "ip_address") String ipAddress,
        @MaxMindDbParameter(name = "is_anonymous") Boolean isAnonymous,
        @MaxMindDbParameter(name = "is_anonymous_vpn") Boolean isAnonymousVpn,
        @MaxMindDbParameter(name = "is_hosting_provider") Boolean isHostingProvider,
        @MaxMindDbParameter(name = "is_public_proxy") Boolean isPublicProxy,
        @MaxMindDbParameter(name = "is_residential_proxy") Boolean isResidentialProxy,
        @MaxMindDbParameter(name = "is_tor_exit_node") Boolean isTorExitNode,
        @MaxMindDbParameter(name = "network") Network network,
        @MaxMindDbParameter(name = "network_last_seen") String networkLastSeen,
        @MaxMindDbParameter(name = "provider_name") String providerName
    ) {
        this(anonymizerConfidence, ipAddress, isAnonymous, isAnonymousVpn,
            isHostingProvider, isPublicProxy, isResidentialProxy, isTorExitNode, network,
            networkLastSeen != null ? LocalDate.parse(networkLastSeen) : null,
            providerName);
    }

    /**
     * Constructs an instance of {@code AnonymousPlusResponse} from the values in the
     * response and the specified IP address and network.
     *
     * @param response  the response to copy
     * @param ipAddress the IP address being checked
     * @param network   the network associated with the record
     */
    public AnonymousPlusResponse(
        AnonymousPlusResponse response,
        String ipAddress,
        Network network
    ) {
        this(
            response.getAnonymizerConfidence(),
            ipAddress,
            response.isAnonymous(),
            response.isAnonymousVpn(),
            response.isHostingProvider(),
            response.isPublicProxy(),
            response.isResidentialProxy(),
            response.isTorExitNode(),
            network,
            response.getNetworkLastSeen(),
            response.getProviderName()
        );
    }

    /**
     * @return A score ranging from 1 to 99 that is our percent confidence that the network is
     *     currently part of an actively used VPN service.
     */
    @JsonProperty
    public Integer getAnonymizerConfidence() {
        return anonymizerConfidence;
    }

    /**
     * @return The last day that the network was sighted in our analysis of anonymized networks.
     */
    @JsonProperty
    public LocalDate getNetworkLastSeen() {
        return networkLastSeen;
    }

    /**
     * @return The name of the VPN provider (e.g., NordVPN, SurfShark, etc.) associated with the
     *     network.
     */
    @JsonProperty
    public String getProviderName() {
        return providerName;
    }
}
