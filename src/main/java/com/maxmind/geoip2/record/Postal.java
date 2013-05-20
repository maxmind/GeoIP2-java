/**
 * 
 */
package com.maxmind.geoip2.record;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author greg
 * 
 */
public class Postal {
    @JsonProperty
    private String code;

    @JsonProperty
    private Integer confidence;

    /**
     * 
     */
    public Postal() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @return the postal code
     */
    public String getCode() {
        return this.code;
    }

    /**
     * @return the postal confidence
     */
    public Integer getConfidence() {
        return this.confidence;
    }

    @Override
    public String toString() {
        return this.code != null ? this.code : "";
    }
}
