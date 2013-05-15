package com.maxmind.geoip2.record;

import java.lang.*;
import java.util.*;
import java.io.*;
import org.json.*;

public abstract class RecordWithNames {

    private HashMap<String, String> names;
    private int geoNameId;
    private Integer confidence;

    protected RecordWithNames(JSONObject json) throws JSONException {
        geoNameId = json.getInt("geoname_id");
        names = new HashMap<String, String>();
        if (json.has("names")) {
            JSONObject jnames = json.getJSONObject("names");
            for (Iterator<String> i = jnames.keys(); i.hasNext();) {
                String key = i.next();
                String value = jnames.getString(key);
                names.put(key, value);
            }
        }
        if (json.has("confidence")) {
            confidence = new Integer(json.getInt("confidence"));
        }
    }

    protected RecordWithNames() {
        names = new HashMap<String, String>();
    }

    public String getName(String l) {
        return names.get(l);
    }

    public int getGeoNameId() {
        return geoNameId;
    }

    public Integer getConfidence() {
        return confidence;
    }
}
