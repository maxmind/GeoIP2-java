package com.maxmind.geoip2.model;

import java.util.*;
import org.json.*;
import com.maxmind.geoip2.record.*;

public class City extends Country {
    private com.maxmind.geoip2.record.City city;
    private Location location;
    private RepresentedCountry representedCountry;
    private ArrayList<Subdivision> subdivisionsList;

    public City(JSONObject json) {
        super(json);
        try {
            if (json.has("city")) {
                JSONObject jcity = json.getJSONObject("city");
                city = new com.maxmind.geoip2.record.City(jcity);
            } else {
                city = new com.maxmind.geoip2.record.City();
            }
            if (json.has("location")) {
                JSONObject jlocation = json.getJSONObject("location");
                location = new Location(jlocation);
            } else {
                location = new Location();
            }
            if (json.has("represented_country")) {
                JSONObject rcountry = json.getJSONObject("represented_country");
                representedCountry = new RepresentedCountry(rcountry);
            } else {
                representedCountry = new RepresentedCountry();
            }
            subdivisionsList = new ArrayList<Subdivision>();
            if (json.has("subdivisions")) {
                JSONArray subdivisionsArray = json.getJSONArray("subdivisions");
                int length = subdivisionsArray.length();
                for (int i = 0; i < length; i++) {
                    JSONObject jsubdivision = subdivisionsArray
                            .getJSONObject(i);
                    subdivisionsList.add(new Subdivision(jsubdivision));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public com.maxmind.geoip2.record.City getCity() {
        return city;
    }

    public Location getLocation() {
        return location;
    }

    public RepresentedCountry getRepresentedCountry() {
        return representedCountry;
    }

    public ArrayList<Subdivision> getSubdivisionsList() {
        return subdivisionsList;
    }
}
