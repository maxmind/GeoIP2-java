package com.maxmind.geoip2.record;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maxmind.geoip2.JsonSerializable;
import java.time.LocalDate;

/**
 * <p>
 * Contains data for the anonymizer record associated with an IP address.
 * </p>
 * <p>
 * This record is returned by the GeoIP2 Precision Insights web service.
 * </p>
 *
 * @param confidence A score ranging from 1 to 99 that is our percent confidence that the
 *                   network is currently part of an actively used VPN service. This is only
 *                   available from the GeoIP2 Precision Insights web service.
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
 * @param networkLastSeen The last day that the network was sighted in our analysis of anonymized
 *                        networks. This is only available from the GeoIP2 Precision Insights web
 *                        service.
 * @param providerName The name of the VPN provider (e.g., NordVPN, SurfShark, etc.) associated
 *                     with the network. This is only available from the GeoIP2 Precision Insights
 *                     web service.
 */
public record Anonymizer(
    @JsonProperty("confidence")
    Integer confidence,

    @JsonProperty("is_anonymous")
    boolean isAnonymous,

    @JsonProperty("is_anonymous_vpn")
    boolean isAnonymousVpn,

    @JsonProperty("is_hosting_provider")
    boolean isHostingProvider,

    @JsonProperty("is_public_proxy")
    boolean isPublicProxy,

    @JsonProperty("is_residential_proxy")
    boolean isResidentialProxy,

    @JsonProperty("is_tor_exit_node")
    boolean isTorExitNode,

    @JsonProperty("network_last_seen")
    LocalDate networkLastSeen,

    @JsonProperty("provider_name")
    String providerName
) implements JsonSerializable {

    /**
     * Constructs an {@code Anonymizer} record with {@code null} values for all the nullable
     * fields and {@code false} for all boolean fields.
     */
    public Anonymizer() {
        this(null, false, false, false, false, false, false, null, null);
    }
}
