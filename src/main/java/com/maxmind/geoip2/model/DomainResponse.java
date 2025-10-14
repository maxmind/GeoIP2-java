package com.maxmind.geoip2.model;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.maxmind.db.MaxMindDbParameter;
import com.maxmind.db.Network;
import com.maxmind.geoip2.JsonSerializable;
import com.maxmind.geoip2.NetworkDeserializer;

/**
 * This class provides the GeoIP2 Domain model.
 *
 * @param domain The second level domain associated with the IP address. This will be something
 *               like "example.com" or "example.co.uk", not "foo.example.com".
 * @param ipAddress The IP address that the data in the model is for.
 * @param network The network associated with the record. In particular, this is the largest
 *                network where all the fields besides IP address have the same value.
 */
public record DomainResponse(
    @JsonProperty("domain")
    @MaxMindDbParameter(name = "domain")
    String domain,

    @JsonProperty("ip_address")
    @MaxMindDbParameter(name = "ip_address")
    String ipAddress,

    @JsonProperty("network")
    @JsonDeserialize(using = NetworkDeserializer.class)
    @MaxMindDbParameter(name = "network")
    Network network
) implements JsonSerializable {

    /**
     * Constructs an instance of {@code DomainResponse} with only required parameters.
     *
     * @param response the response
     * @param ipAddress the IP address that the data in the model is for.
     * @param network the network associated with the record.
     */
    public DomainResponse(
        DomainResponse response,
        String ipAddress,
        Network network
    ) {
        this(response.domain(), ipAddress, network);
    }

    /**
     * @return The second level domain associated with the IP address. This
     * will be something like "example.com" or "example.co.uk", not
     * "foo.example.com".
     * @deprecated Use {@link #domain()} instead. This method will be removed in 6.0.0.
     */
    @Deprecated(since = "5.0.0", forRemoval = true)
    public String getDomain() {
        return domain();
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
}
