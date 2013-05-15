package com.maxmind.geoip2.record;

import org.json.*;

public class Subdivision extends RecordWithNames {
    private String isoCode;

    public Subdivision(JSONObject jcountry) throws JSONException {
        super(jcountry);
        isoCode = jcountry.getString("iso_code");
    }

    public String getCode() {
        return isoCode;
    }
}
