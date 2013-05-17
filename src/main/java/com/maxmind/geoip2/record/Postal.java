/**
 * 
 */
package com.maxmind.geoip2.record;

import com.google.api.client.util.Key;

/**
 * @author greg
 * 
 */
public class Postal {
    @Key
    private String code;

    @Key
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
}
