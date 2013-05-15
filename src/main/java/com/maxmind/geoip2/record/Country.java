package com.maxmind.geoip2.record;

import org.json.*;

public class Country extends RecordWithNames {
    private String isoCode;

    public Country(JSONObject jcountry) throws JSONException {
        super(jcountry);
        isoCode = jcountry.getString("iso_code");
    }

    public Country() {
        super();
    }

    public String getIsoCode() {
        return isoCode;
    }
}
