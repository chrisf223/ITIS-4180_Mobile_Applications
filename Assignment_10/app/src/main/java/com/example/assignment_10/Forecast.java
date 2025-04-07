package com.example.assignment_10;

import org.json.JSONException;
import org.json.JSONObject;

public class Forecast {
    String time, temp, tempUnit, precipitation, shortForecast, wind, icon;

    public Forecast() {

    }

    public Forecast(JSONObject jsonObject) throws JSONException {
        this.time = jsonObject.getString("startTime");
        this.temp = jsonObject.getString("temperature");
        this.tempUnit = jsonObject.getString("temperatureUnit");
        JSONObject popObj = jsonObject.getJSONObject("probabilityOfPrecipitation");
        this.precipitation = popObj.isNull("value") ? "0%" : popObj.getInt("value") + "%";
        this.shortForecast = jsonObject.getString("shortForecast");
        this.wind = jsonObject.getString("windSpeed");
        this.icon = jsonObject.getString("icon");
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getTempUnit() {
        return tempUnit;
    }

    public void setTempUnit(String tempUnit) {
        this.tempUnit = tempUnit;
    }

    public String getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(String precipitation) {
        this.precipitation = precipitation;
    }

    public String getShortForecast() {
        return shortForecast;
    }

    public void setShortForecast(String shortForecast) {
        this.shortForecast = shortForecast;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
