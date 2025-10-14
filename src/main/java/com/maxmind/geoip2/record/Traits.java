package com.maxmind.geoip2.record;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.maxmind.db.MaxMindDbConstructor;
import com.maxmind.db.MaxMindDbParameter;
import com.maxmind.db.Network;
import com.maxmind.geoip2.JsonSerializable;
import com.maxmind.geoip2.NetworkDeserializer;
import com.maxmind.geoip2.model.ConnectionTypeResponse.ConnectionType;

/**
 * Contains data for the traits record associated with an IP address.
 *
 * @param autonomousSystemNumber The <a
 *                               href="https://en.wikipedia.org/wiki/Autonomous_system_(Internet)"
 *                               >autonomous system number</a> associated with the IP address.
 *                               This is only available from the City Plus and Insights web
 *                               services and the Enterprise database.
 * @param autonomousSystemOrganization The organization associated with the registered <a
 *                                     href="https://en.wikipedia.org/wiki/Autonomous_system_(Internet)"
 *                                     >autonomous system number</a> for the IP address. This is
 *                                     only available from the City Plus and Insights web services
 *                                     and the Enterprise database.
 * @param connectionType The connection type of the IP address. This is only available from the
 *                       City Plus and Insights web services and the Enterprise database.
 * @param domain The second level domain associated with the IP address. This will be something
 *               like "example.com" or "example.co.uk", not "foo.example.com". This is only
 *               available from the City Plus and Insights web services and the Enterprise
 *               database.
 * @param ipAddress The IP address that the data in the model is for. If you performed a "me"
 *                  lookup against the web service, this will be the externally routable IP
 *                  address for the system the code is running on. If the system is behind a
 *                  NAT, this may differ from the IP address locally assigned to it.
 * @param isAnonymous This is true if the IP address belongs to any sort of anonymous network.
 * @param isAnonymousVpn This is true if the IP address belongs to an anonymous VPN system.
 * @param isAnycast This is true if the IP address is an anycast address.
 * @param isHostingProvider This is true if the IP address belongs to a hosting provider.
 * @param isLegitimateProxy This is true if the IP address belongs to a legitimate proxy.
 * @param isPublicProxy This is true if the IP address belongs to a public proxy.
 * @param isResidentialProxy This is true if the IP address is on a suspected anonymizing network
 *                           and belongs to a residential ISP.
 * @param isTorExitNode This is true if the IP address is a Tor exit node.
 * @param isp The name of the ISP associated with the IP address. This is only available from
 *            the City Plus and Insights web services and the Enterprise database.
 * @param mobileCountryCode The <a href="https://en.wikipedia.org/wiki/Mobile_country_code">
 *                          mobile country code (MCC)</a> associated with the IP address and ISP.
 *                          This is available from the City Plus and Insights web services and
 *                          the Enterprise database.
 * @param mobileNetworkCode The <a href="https://en.wikipedia.org/wiki/Mobile_country_code">
 *                          mobile network code (MNC)</a> associated with the IP address and ISP.
 *                          This is available from the City Plus and Insights web services and
 *                          the Enterprise database.
 * @param network The network associated with the record. In particular, this is the largest
 *                network where all the fields besides IP address have the same value.
 * @param organization The name of the organization associated with the IP address. This is only
 *                     available from the City Plus and Insights web services and the Enterprise
 *                     database.
 * @param userType The user type associated with the IP address. This can be one of the following
 *                 values: business, cafe, cellular, college, consumer_privacy_network,
 *                 content_delivery_network, dialup, government, hosting, library, military,
 *                 residential, router, school, search_engine_spider, traveler. This is only
 *                 available from the Insights web service and the Enterprise database.
 * @param userCount The estimated number of users sharing the IP address/network during the past
 *                  24 hours. For IPv4, the count is for the individual IP address. For IPv6, the
 *                  count is for the /64 network. This is only available from the Insights web
 *                  service.
 * @param staticIpScore The static IP score of the IP address. This is an indicator of how static
 *                      or dynamic an IP address is. This is only available from the Insights web
 *                      service.
 */
public record Traits(
    @JsonProperty("autonomous_system_number")
    @MaxMindDbParameter(name = "autonomous_system_number")
    Long autonomousSystemNumber,

    @JsonProperty("autonomous_system_organization")
    @MaxMindDbParameter(name = "autonomous_system_organization")
    String autonomousSystemOrganization,

    @JsonProperty("connection_type")
    @MaxMindDbParameter(name = "connection_type")
    ConnectionType connectionType,

    @JsonProperty("domain")
    @MaxMindDbParameter(name = "domain")
    String domain,

    @JsonProperty("ip_address")
    @MaxMindDbParameter(name = "ip_address")
    String ipAddress,

    @JsonProperty("is_anonymous")
    @MaxMindDbParameter(name = "is_anonymous", useDefault = true)
    boolean isAnonymous,

    @JsonProperty("is_anonymous_vpn")
    @MaxMindDbParameter(name = "is_anonymous_vpn", useDefault = true)
    boolean isAnonymousVpn,

    @JsonProperty("is_anycast")
    @MaxMindDbParameter(name = "is_anycast", useDefault = true)
    boolean isAnycast,

    @JsonProperty("is_hosting_provider")
    @MaxMindDbParameter(name = "is_hosting_provider", useDefault = true)
    boolean isHostingProvider,

    @JsonProperty("is_legitimate_proxy")
    @MaxMindDbParameter(name = "is_legitimate_proxy", useDefault = true)
    boolean isLegitimateProxy,

    @JsonProperty("is_public_proxy")
    @MaxMindDbParameter(name = "is_public_proxy", useDefault = true)
    boolean isPublicProxy,

    @JsonProperty("is_residential_proxy")
    @MaxMindDbParameter(name = "is_residential_proxy", useDefault = true)
    boolean isResidentialProxy,

    @JsonProperty("is_tor_exit_node")
    @MaxMindDbParameter(name = "is_tor_exit_node", useDefault = true)
    boolean isTorExitNode,

    @JsonProperty("isp")
    @MaxMindDbParameter(name = "isp")
    String isp,

    @JsonProperty("mobile_country_code")
    @MaxMindDbParameter(name = "mobile_country_code")
    String mobileCountryCode,

    @JsonProperty("mobile_network_code")
    @MaxMindDbParameter(name = "mobile_network_code")
    String mobileNetworkCode,

    @JsonProperty("network")
    @JsonDeserialize(using = NetworkDeserializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @MaxMindDbParameter(name = "network")
    Network network,

    @JsonProperty("organization")
    @MaxMindDbParameter(name = "organization")
    String organization,

    @JsonProperty("user_type")
    @MaxMindDbParameter(name = "user_type")
    String userType,

    @JsonProperty("user_count")
    @MaxMindDbParameter(name = "user_count")
    Integer userCount,

    @JsonProperty("static_ip_score")
    @MaxMindDbParameter(name = "static_ip_score")
    Double staticIpScore
) implements JsonSerializable {

    /**
     * Constructs an instance of {@code Traits}.
     */
    public Traits() {
        this(null, null, (ConnectionType) null, null,
            null, false, false, false, false,
            false, false, false, false, null,
            null, null, null, null, null, null, null);
    }

    /**
     * Constructs an instance of {@code Traits}.
     *
     * @param ipAddress the IP address
     * @param network   the network
     */
    public Traits(String ipAddress, Network network) {
        this(null, null, (ConnectionType) null, null,
            ipAddress, false, false, false, false,
            false, false, false, false, null,
            null, null, network, null, null, null, null);
    }

    /**
     * Constructs an instance of {@code Traits} from MaxMind database with type conversions.
     *
     * @param autonomousSystemNumber the autonomous system number
     * @param autonomousSystemOrganization the autonomous system organization
     * @param connectionType the connection type as a string
     * @param domain the domain
     * @param ipAddress the IP address
     * @param isAnonymous the anonymous flag
     * @param isAnonymousVpn the anonymous VPN flag
     * @param isAnycast the anycast flag
     * @param isHostingProvider the hosting provider flag
     * @param isLegitimateProxy the legitimate proxy flag
     * @param isPublicProxy the public proxy flag
     * @param isResidentialProxy the residential proxy flag
     * @param isTorExitNode the Tor exit node flag
     * @param isp the ISP
     * @param mobileCountryCode the mobile country code
     * @param mobileNetworkCode the mobile network code
     * @param network the network
     * @param organization the organization
     * @param userType the user type
     * @param userCount the user count
     * @param staticIpScore the static IP score
     */
    @MaxMindDbConstructor
    public Traits(
        @MaxMindDbParameter(name = "autonomous_system_number") Long autonomousSystemNumber,
        @MaxMindDbParameter(name = "autonomous_system_organization")
        String autonomousSystemOrganization,
        @MaxMindDbParameter(name = "connection_type") String connectionType,
        @MaxMindDbParameter(name = "domain") String domain,
        @MaxMindDbParameter(name = "ip_address") String ipAddress,
        @MaxMindDbParameter(name = "is_anonymous", useDefault = true)
        boolean isAnonymous,
        @MaxMindDbParameter(name = "is_anonymous_vpn", useDefault = true)
        boolean isAnonymousVpn,
        @MaxMindDbParameter(name = "is_anycast", useDefault = true)
        boolean isAnycast,
        @MaxMindDbParameter(name = "is_hosting_provider", useDefault = true)
        boolean isHostingProvider,
        @MaxMindDbParameter(name = "is_legitimate_proxy", useDefault = true)
        boolean isLegitimateProxy,
        @MaxMindDbParameter(name = "is_public_proxy", useDefault = true)
        boolean isPublicProxy,
        @MaxMindDbParameter(name = "is_residential_proxy", useDefault = true)
        boolean isResidentialProxy,
        @MaxMindDbParameter(name = "is_tor_exit_node", useDefault = true)
        boolean isTorExitNode,
        @MaxMindDbParameter(name = "isp") String isp,
        @MaxMindDbParameter(name = "mobile_country_code") String mobileCountryCode,
        @MaxMindDbParameter(name = "mobile_network_code") String mobileNetworkCode,
        @MaxMindDbParameter(name = "network") Network network,
        @MaxMindDbParameter(name = "organization") String organization,
        @MaxMindDbParameter(name = "user_type") String userType,
        @MaxMindDbParameter(name = "user_count") Integer userCount,
        @MaxMindDbParameter(name = "static_ip_score") Double staticIpScore
    ) {
        this(
            autonomousSystemNumber,
            autonomousSystemOrganization,
            ConnectionType.fromString(connectionType),
            domain,
            ipAddress,
            isAnonymous,
            isAnonymousVpn,
            isAnycast,
            isHostingProvider,
            isLegitimateProxy,
            isPublicProxy,
            isResidentialProxy,
            isTorExitNode,
            isp,
            mobileCountryCode,
            mobileNetworkCode,
            network,
            organization,
            userType,
            userCount,
            staticIpScore
        );
    }

    /**
     * Constructs an instance of {@code Traits}.
     *
     * @param traits the traits
     * @param ipAddress the IP address
     * @param network the network
     */
    public Traits(
        Traits traits,
        String ipAddress,
        Network network
    ) {
        this(
            traits.autonomousSystemNumber(),
            traits.autonomousSystemOrganization(),
            traits.connectionType(),
            traits.domain(),
            ipAddress,
            traits.isAnonymous(),
            traits.isAnonymousVpn(),
            traits.isAnycast(),
            traits.isHostingProvider(),
            traits.isLegitimateProxy(),
            traits.isPublicProxy(),
            traits.isResidentialProxy(),
            traits.isTorExitNode(),
            traits.isp(),
            traits.mobileCountryCode(),
            traits.mobileNetworkCode(),
            network,
            traits.organization(),
            traits.userType(),
            traits.userCount(),
            traits.staticIpScore()
        );
    }

    /**
     * @return The <a
     * href="https://en.wikipedia.org/wiki/Autonomous_system_(Internet)"
     * >autonomous system number</a> associated with the IP address. This
     * is only available from the City Plus and Insights web services and
     * the Enterprise database.
     * @deprecated Use {@link #autonomousSystemNumber()} instead. This method will be
     *     removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    @JsonProperty("autonomous_system_number")
    public Long getAutonomousSystemNumber() {
        return autonomousSystemNumber();
    }

    /**
     * @return The organization associated with the registered <a
     * href="https://en.wikipedia.org/wiki/Autonomous_system_(Internet)"
     * >autonomous system number</a> for the IP address. This is only
     * available from the City Plus and Insights web services and the
     * Enterprise database.
     * @deprecated Use {@link #autonomousSystemOrganization()} instead. This method will be
     *     removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    @JsonProperty("autonomous_system_organization")
    public String getAutonomousSystemOrganization() {
        return autonomousSystemOrganization();
    }

    /**
     * @return The connection type of the IP address. This is only
     * available from the City Plus and Insights web services and the
     * Enterprise database.
     * @deprecated Use {@link #connectionType()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    @JsonProperty("connection_type")
    public ConnectionType getConnectionType() {
        return connectionType();
    }

    /**
     * @return The static IP score of the IP address. This is an indicator of
     * how static or dynamic an IP address is. This is only available from
     * the Insights web service.
     * @deprecated Use {@link #staticIpScore()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    @JsonProperty("static_ip_score")
    public Double getStaticIpScore() {
        return staticIpScore();
    }

    /**
     * @return The estimated number of users sharing the IP address/network
     * during the past 24 hours. For IPv4, the count is for the individual
     * IP address. For IPv6, the count is for the /64 network. This is only
     * available from the Insights web service.
     * @deprecated Use {@link #userCount()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    @JsonProperty("user_count")
    public Integer getUserCount() {
        return userCount();
    }

    /**
     * @return The second level domain associated with the IP address. This
     * will be something like "example.com" or "example.co.uk", not
     * "foo.example.com". This is only available from the City Plus and
     * Insights web services and the Enterprise database.
     * @deprecated Use {@link #domain()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    @JsonProperty
    public String getDomain() {
        return domain();
    }

    /**
     * @return The IP address that the data in the model is for. If you
     * performed a "me" lookup against the web service, this will be the
     * externally routable IP address for the system the code is running
     * on. If the system is behind a NAT, this may differ from the IP
     * address locally assigned to it.
     * @deprecated Use {@link #ipAddress()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    @JsonProperty("ip_address")
    public String getIpAddress() {
        return ipAddress();
    }

    /**
     * @return The name of the ISP associated with the IP address. This
     * is only available from the City Plus and Insights web services and
     * the Enterprise database.
     * @deprecated Use {@link #isp()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    public String getIsp() {
        return isp();
    }

    /**
     * @return The <a href="https://en.wikipedia.org/wiki/Mobile_country_code">
     * mobile country code (MCC)</a> associated with the IP address and ISP.
     * This is available from the City Plus and Insights web services and the
     * Enterprise database.
     * @deprecated Use {@link #mobileCountryCode()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    @JsonProperty("mobile_country_code")
    public String getMobileCountryCode() {
        return mobileCountryCode();
    }

    /**
     * @return The <a href="https://en.wikipedia.org/wiki/Mobile_country_code">
     * mobile network code (MNC)</a> associated with the IP address and ISP.
     * This is available from the City Plus and Insights web services and the
     * Enterprise database.
     * @deprecated Use {@link #mobileNetworkCode()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    @JsonProperty("mobile_network_code")
    public String getMobileNetworkCode() {
        return mobileNetworkCode();
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
     * @return The name of the organization associated with the IP address.
     * This is only available from the City Plus and Insights web services and
     * the Enterprise database.
     * @deprecated Use {@link #organization()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    @JsonProperty
    public String getOrganization() {
        return organization();
    }

    /**
     * @return <p>
     * The user type associated with the IP address. This can be one of
     * the following values:
     * </p>
     * <ul>
     * <li>business
     * <li>cafe
     * <li>cellular
     * <li>college
     * <li>consumer_privacy_network
     * <li>content_delivery_network
     * <li>dialup
     * <li>government
     * <li>hosting
     * <li>library
     * <li>military
     * <li>residential
     * <li>router
     * <li>school
     * <li>search_engine_spider
     * <li>traveler
     * </ul>
     * <p>
     * This is only available from the Insights web service and the Enterprise
     * database.
     * </p>
     * @deprecated Use {@link #userType()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    @JsonProperty("user_type")
    public String getUserType() {
        return userType();
    }
}
