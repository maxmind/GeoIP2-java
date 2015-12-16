package com.maxmind.geoip2.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class provides the GeoIP2 Domain model.
 */
public class DomainResponse extends AbstractResponse {

    private final String domain;
    private final String ipAddress;

    public DomainResponse(
            @JsonProperty("domain") String domain,
            @JsonProperty("ip_address") String ipAddress
    ) {
        this.domain = domain;
        this.ipAddress = ipAddress;
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

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "DomainResponse [domain=" + this.domain + ", ipAddress="
                + this.ipAddress + "]";
    }
}
