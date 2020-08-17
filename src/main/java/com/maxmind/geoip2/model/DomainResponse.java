package com.maxmind.geoip2.model;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.maxmind.db.Network;
import com.maxmind.geoip2.NetworkDeserializer;

/**
 * This class provides the GeoIP2 Domain model.
 */
public class DomainResponse extends AbstractResponse {

    private final String domain;
    private final String ipAddress;
    private final Network network;

    DomainResponse() {
        this(null, null);
    }

    public DomainResponse(
            String domain,
            String ipAddress
    ) {
        this(domain, ipAddress, null);
    }

    public DomainResponse(
            @JsonProperty("domain") String domain,
            @JacksonInject("ip_address") @JsonProperty("ip_address") String ipAddress,
            @JacksonInject("network") @JsonProperty("network") @JsonDeserialize(using = NetworkDeserializer.class) Network network
    ) {
        this.domain = domain;
        this.ipAddress = ipAddress;
        this.network = network;
    }

    public DomainResponse(
            DomainDatabaseModel model,
            String ipAddress,
            Network network
    ) {
        this(model.getDomain(), ipAddress, network);
    }

    /**
     * @return the The second level domain associated with the IP address. This
     * will be something like "example.com" or "example.co.uk", not
     * "foo.example.com".
     */
    public String getDomain() {
        return this.domain;
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
