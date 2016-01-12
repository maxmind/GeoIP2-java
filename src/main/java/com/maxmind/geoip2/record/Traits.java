package com.maxmind.geoip2.record;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <p>
 * Contains data for the traits record associated with an IP address.
 * </p>
 * <p>
 * This record is returned by all the end points.
 * </p>
 */
public final class Traits extends AbstractRecord  {

    private final Integer autonomousSystemNumber;
    private final String autonomousSystemOrganization;
    private final String domain;
    private final String ipAddress;
    private final boolean anonymousProxy;
    private final boolean satelliteProvider;
    private final String isp;
    private final String organization;
    private final String userType;

    public Traits() {
        this(null, null, null, null, false, false, null, null, null);
    }

    public Traits(String ipAddress) {
        this(null, null, null, ipAddress, false, false, null, null, null);
    }

    public Traits(
            @JsonProperty("autonomous_system_number") Integer autonomousSystemNumber,
            @JsonProperty("autonomous_system_organization") String autonomousSystemOrganization,
            @JsonProperty("domain") String domain,
            @JacksonInject("ip_address") @JsonProperty("ip_address") String ipAddress,
            @JsonProperty("is_anonymous_proxy") boolean anonymousProxy,
            @JsonProperty("is_satellite_provider") boolean satelliteProvider,
            @JsonProperty("isp") String isp,
            @JsonProperty("organization") String organization,
            @JsonProperty("user_type") String userType
    ) {
        this.autonomousSystemNumber = autonomousSystemNumber;
        this.autonomousSystemOrganization = autonomousSystemOrganization;
        this.domain = domain;
        this.ipAddress = ipAddress;
        this.anonymousProxy = anonymousProxy;
        this.satelliteProvider = satelliteProvider;
        this.isp = isp;
        this.organization = organization;
        this.userType = userType;
    }

    /**
     * @return This is true if the IP is an anonymous proxy. This attribute is
     * returned by all end points.
     * @see <a href="http://dev.maxmind.com/faq/geoip#anonproxy">MaxMind's GeoIP
     * FAQ</a>
     * @deprecated Use our
     * <a href="https://www.maxmind.com/en/geoip2-anonymous-ip-database">GeoIP2
     * Anonymous IP database</a> instead.
     */
    @Deprecated
    @JsonProperty("is_anonymous_proxy")
    public boolean isAnonymousProxy() {
        return this.anonymousProxy;
    }

    /**
     * @return The <a
     * href="http://en.wikipedia.org/wiki/Autonomous_system_(Internet)"
     * >autonomous system number</a> associated with the IP address.
     * This attribute is only available from the City and Insights web
     * service end points.
     */
    @JsonProperty("autonomous_system_number")
    public Integer getAutonomousSystemNumber() {
        return this.autonomousSystemNumber;
    }


    /**
     * @return The name of the ISP associated with the IP address. This
     * attribute is only available from the City and Insights web
     * service end points.
     */
    public String getIsp() {
        return this.isp;
    }

    /**
     * @return The IP address that the data in the model is for. If you
     * performed a "me" lookup against the web service, this will be the
     * externally routable IP address for the system the code is running
     * on. If the system is behind a NAT, this may differ from the IP
     * address locally assigned to it. This attribute is returned by all
     * end points.
     */
    @JsonProperty("ip_address")
    public String getIpAddress() {
        return this.ipAddress;
    }

    /**
     * @return This is true if the IP belong to a satellite internet provider.
     * This attribute is returned by all end points.
     * @deprecated Due to increased mobile usage, we have insufficient data to
     * maintain this field.
     */
    @Deprecated
    @JsonProperty("is_satellite_provider")
    public boolean isSatelliteProvider() {
        return this.satelliteProvider;
    }

    /**
     * @return The organization associated with the registered <a
     * href="http://en.wikipedia.org/wiki/Autonomous_system_(Internet)"
     * >autonomous system number</a> for the IP address. This attribute
     * is only available from the City and Insights web service end
     * points.
     */
    @JsonProperty("autonomous_system_organization")
    public String getAutonomousSystemOrganization() {
        return this.autonomousSystemOrganization;
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
     * This attribute is only available from the Insights end point.
     * </p>
     */
    @JsonProperty("user_type")
    public String getUserType() {
        return this.userType;
    }

    /**
     * @return The name of the organization associated with the IP address. This
     * attribute is only available from the City and Insights web
     * service end points.
     */
    @JsonProperty
    public String getOrganization() {
        return this.organization;
    }

    /**
     * @return The second level domain associated with the IP address. This will
     * be something like "example.com" or "example.co.uk", not
     * "foo.example.com". This attribute is only available from the City
     * and Insights web service end points.
     */
    @JsonProperty
    public String getDomain() {
        return this.domain;
    }
}
