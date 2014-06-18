package com.maxmind.geoip2.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class provides the GeoIP2 Domain model.
 */
public class DomainResponse extends AbstractResponse {

    @JsonProperty
    private String domain;

    @JsonProperty("ip_address")
    private String ipAddress;

    public DomainResponse() {
    }

    /**
     * @return the The second level domain associated with the IP address. This
     *         will be something like "example.com" or "example.co.uk", not
     *         "foo.example.com".
     */
    public String getDomain() {
        return this.domain;
    }

    /**
     * @return The IP address that the data in the model is for.
     */
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
