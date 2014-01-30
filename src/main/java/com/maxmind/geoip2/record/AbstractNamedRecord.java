package com.maxmind.geoip2.record;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Abstract class for records with name maps.
 */
public abstract class AbstractNamedRecord implements Serializable {
    private static final long serialVersionUID = -5545449318673747357L;

    @JsonProperty
    private HashMap<String, String> names = new HashMap<String, String>();
    @JsonProperty("geoname_id")
    private Integer geoNameId;

    @JacksonInject("locales")
    private List<String> locales = new ArrayList<String>();

    AbstractNamedRecord() {
    }

    /**
     * @return The GeoName ID for the city. This attribute is returned by all
     *         end points.
     */
    public Integer getGeoNameId() {
        return this.geoNameId;
    }

    /**
     * @return The name of the city based on the locales list passed to the
     *         {@link com.maxmind.geoip2.WebServiceClient} constructor. This
     *         attribute is returned by all end points.
     */
    public String getName() {
        for (String lang : this.locales) {
            if (this.names.containsKey(lang)) {
                return this.names.get(lang);
            }
        }
        return null;
    }

    /**
     * @return A {@link Map} from locale codes to the name in that locale. This
     *         attribute is returned by all end points.
     */
    public Map<String, String> getNames() {
        return new HashMap<String, String>(this.names);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.getName() != null ? this.getName() : "";
    }
}
