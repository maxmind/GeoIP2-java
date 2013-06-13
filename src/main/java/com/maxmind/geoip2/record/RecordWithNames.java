package com.maxmind.geoip2.record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Abstract class for records with name maps.
 */
public abstract class RecordWithNames {
    @JsonProperty
    private HashMap<String, String> names = new HashMap<String, String>();
    @JsonProperty("geoname_id")
    private Integer geoNameId;

    @JacksonInject("languages")
    private List<String> languages = new ArrayList<String>();

    RecordWithNames() {
    }

    /**
     * @return The GeoName ID for the city. This attribute is returned by all
     *         end points.
     */
    public Integer getGeoNameId() {
        return this.geoNameId;
    }

    /**
     * @return The name of the city based on the languages list passed to the
     *         {@link com.maxmind.geoip2.webservice.Client} constructor. This
     *         attribute is returned by all end points.
     */
    public String getName() {
        for (String lang : this.languages) {
            if (this.names.containsKey(lang)) {
                return this.names.get(lang);
            }
        }
        return null;
    }

    /**
     * @return A {@link Map} from language codes to the name in that language
     *         names. This attribute is returned by all end points.
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
