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
            JSONObject jcity = json.getJSONObject("city");
            city = new com.maxmind.geoip2.record.City(jcity);
        } catch (JSONException e) {
            city = new com.maxmind.geoip2.record.City();
        }

        try {
            JSONObject jlocation = json.getJSONObject("location");
            location = new Location(jlocation);
        } catch (JSONException e) {
            location = new Location();
        }

        try {
            JSONObject rcountry = json.getJSONObject("represented_country");
            representedCountry = new RepresentedCountry(rcountry);
        } catch (JSONException e) {
            representedCountry = new RepresentedCountry();
        }
        subdivisionsList = new ArrayList<Subdivision>();

        try {
            JSONArray subdivisionsArray = json.getJSONArray("subdivisions");
            int length = subdivisionsArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject jsubdivision = subdivisionsArray.getJSONObject(i);
                subdivisionsList.add(new Subdivision(jsubdivision));
            }
        } catch (JSONException e) {
            // Do nothing as we already gave the array
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
