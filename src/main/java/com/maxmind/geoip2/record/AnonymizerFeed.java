package com.maxmind.geoip2.record;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maxmind.geoip2.JsonSerializable;
import java.time.LocalDate;

/**
 * <p>
 * Contains data for one type of anonymizer detection, currently residential proxies. Additional
 * feeds, such as VPNs, mobile networks, and hosting or datacenter providers, may be added to the
 * {@link Anonymizer} record in the future using this same record type.
 * </p>
 * <p>
 * This record is returned by the GeoIP Insights web service.
 * </p>
 *
 * @param confidence A score ranging from 1 to 99 that represents our percent confidence that
 *                   the network is currently part of this anonymizer feed. This is only
 *                   available from the GeoIP Insights web service.
 * @param networkLastSeen The last day that the network was sighted in our analysis of this
 *                        anonymizer feed. This is only available from the GeoIP Insights web
 *                        service.
 * @param providerName The name of the provider associated with the network in this anonymizer
 *                     feed. This is only available from the GeoIP Insights web service.
 */
public record AnonymizerFeed(
    @JsonProperty("confidence")
    Integer confidence,

    @JsonProperty("network_last_seen")
    LocalDate networkLastSeen,

    @JsonProperty("provider_name")
    String providerName
) implements JsonSerializable {

    /**
     * Constructs an {@code AnonymizerFeed} record with {@code null} values for all fields.
     */
    public AnonymizerFeed() {
        this(null, null, null);
    }
}
