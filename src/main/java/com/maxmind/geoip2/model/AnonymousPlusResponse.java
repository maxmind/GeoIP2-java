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
import java.time.LocalDate;

/**
 * This class provides the GeoIP Anonymous Plus model.
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
 * @param anonymizerConfidence A score ranging from 1 to 99 that is our percent confidence that
 *                             the network is currently part of an actively used VPN service.
 * @param networkLastSeen The last day that the network was sighted in our analysis of anonymized
 *                        networks.
 * @param providerName The name of the VPN provider (e.g., NordVPN, SurfShark, etc.) associated
 *                     with the network.
 */
public record AnonymousPlusResponse(
    @JsonProperty("ip_address")
    @MaxMindDbParameter(name = "ip_address")
    String ipAddress,

    @JsonProperty("is_anonymous")
    @MaxMindDbParameter(name = "is_anonymous")
    boolean isAnonymous,

    @JsonProperty("is_anonymous_vpn")
    @MaxMindDbParameter(name = "is_anonymous_vpn")
    boolean isAnonymousVpn,

    @JsonProperty("is_hosting_provider")
    @MaxMindDbParameter(name = "is_hosting_provider")
    boolean isHostingProvider,

    @JsonProperty("is_public_proxy")
    @MaxMindDbParameter(name = "is_public_proxy")
    boolean isPublicProxy,

    @JsonProperty("is_residential_proxy")
    @MaxMindDbParameter(name = "is_residential_proxy")
    boolean isResidentialProxy,

    @JsonProperty("is_tor_exit_node")
    @MaxMindDbParameter(name = "is_tor_exit_node")
    boolean isTorExitNode,

    @JsonProperty("network")
    @JsonDeserialize(using = NetworkDeserializer.class)
    @MaxMindDbParameter(name = "network")
    Network network,

    @JsonProperty("anonymizer_confidence")
    @MaxMindDbParameter(name = "anonymizer_confidence")
    Integer anonymizerConfidence,

    @JsonProperty("network_last_seen")
    @MaxMindDbParameter(name = "network_last_seen")
    LocalDate networkLastSeen,

    @JsonProperty("provider_name")
    @MaxMindDbParameter(name = "provider_name")
    String providerName
) implements JsonSerializable {

    /**
     * Constructs an instance of {@code AnonymousPlusResponse} with nullable boolean fields
     * and date parsing from MaxMind database.
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
        this(
            ipAddress,
            isAnonymous != null ? isAnonymous : false,
            isAnonymousVpn != null ? isAnonymousVpn : false,
            isHostingProvider != null ? isHostingProvider : false,
            isPublicProxy != null ? isPublicProxy : false,
            isResidentialProxy != null ? isResidentialProxy : false,
            isTorExitNode != null ? isTorExitNode : false,
            network,
            anonymizerConfidence,
            networkLastSeen != null ? LocalDate.parse(networkLastSeen) : null,
            providerName
        );
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
            ipAddress,
            response.isAnonymous(),
            response.isAnonymousVpn(),
            response.isHostingProvider(),
            response.isPublicProxy(),
            response.isResidentialProxy(),
            response.isTorExitNode(),
            network,
            response.anonymizerConfidence(),
            response.networkLastSeen(),
            response.providerName()
        );
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

    /**
     * @return A score ranging from 1 to 99 that is our percent confidence that the network is
     *     currently part of an actively used VPN service.
     * @deprecated Use {@link #anonymizerConfidence()} instead. This method will be removed
     *     in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    @JsonProperty
    public Integer getAnonymizerConfidence() {
        return anonymizerConfidence();
    }

    /**
     * @return The last day that the network was sighted in our analysis of anonymized networks.
     * @deprecated Use {@link #networkLastSeen()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    @JsonProperty
    public LocalDate getNetworkLastSeen() {
        return networkLastSeen();
    }

    /**
     * @return The name of the VPN provider (e.g., NordVPN, SurfShark, etc.) associated with the
     *     network.
     * @deprecated Use {@link #providerName()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    @JsonProperty
    public String getProviderName() {
        return providerName();
    }
}
