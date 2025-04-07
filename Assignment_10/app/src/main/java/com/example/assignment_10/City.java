package com.example.assignment_10;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class City implements Serializable {
    String name, state, lat, lng;

    public City() {

    }

    public City(JSONObject jsonObject) throws JSONException {
        this.name = jsonObject.getString("name");
        this.state = jsonObject.getString("state");
        this.lat = jsonObject.getString("lat");
        this.lng = jsonObject.getString("lng");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
