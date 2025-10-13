package com.maxmind.geoip2;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

/**
 * Interface for record classes that have localized names and GeoName IDs.
 * Provides a default implementation for the name() method that returns the name
 * in the first available locale.
 */
public interface NamedRecord extends JsonSerializable {

    /**
     * @return The GeoName ID for this location.
     */
    @JsonProperty("geoname_id")
    Long geonameId();

    /**
     * @return A {@link Map} from locale codes to the name in that locale.
     */
    @JsonProperty("names")
    Map<String, String> names();

    /**
     * @return The list of locales to use for name lookups.
     */
    @JsonIgnore
    List<String> locales();

    /**
     * @return The name based on the locales list. Returns the name in the first
     * locale for which a name is available. If no name is available in any of the
     * specified locales, returns null.
     */
    @JsonIgnore
    default String name() {
        for (String lang : locales()) {
            if (names().containsKey(lang)) {
                return names().get(lang);
            }
        }
        return null;
    }
}
