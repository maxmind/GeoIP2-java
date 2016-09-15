package com.maxmind.geoip2.record;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract class for records with name maps.
 */
public abstract class AbstractNamedRecord extends AbstractRecord {

    private final Map<String, String> names;
    private final Integer geoNameId;
    private final List<String> locales;

    AbstractNamedRecord() {
        this(null, null, null);
    }

    AbstractNamedRecord(List<String> locales, Integer geoNameId, Map<String, String> names) {
        this.names = names != null ? names : new HashMap<String, String>();
        this.geoNameId = geoNameId;
        this.locales = locales != null ? locales : new ArrayList<String>();
    }

    /**
     * @return The GeoName ID for the city. This attribute is returned by all
     * end points.
     */
    @JsonProperty("geoname_id")
    public Integer getGeoNameId() {
        return this.geoNameId;
    }

    /**
     * @return The name of the city based on the locales list passed to the
     * {@link com.maxmind.geoip2.WebServiceClient} constructor. This
     * attribute is returned by all end points.
     */
    @JsonIgnore
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
     * attribute is returned by all end points.
     */
    @JsonProperty("names")
    public Map<String, String> getNames() {
        return new HashMap<>(this.names);
    }
}
