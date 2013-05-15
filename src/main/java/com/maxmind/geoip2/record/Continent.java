package com.maxmind.geoip2.record;

import java.lang.*;
import java.util.*;
import java.io.*;
import org.json.*;

public class Continent extends RecordWithNames {
    private String continentCode;

    public Continent(JSONObject jcontinent) throws JSONException {
        super(jcontinent);
        continentCode = jcontinent.getString("continent_code");
    }

    public Continent() {
        super();
    }

    public String getCode() {
        return continentCode;
    }
}
