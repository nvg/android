package com.londonappbrewery.climapm;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherDataModel {

    private String temp;
    private int condition;
    private String city;
    private String icon;

    public WeatherDataModel() {
        super();
    }

    public WeatherDataModel(String temp, int condition, String city, String icon) {
        this();

        this.temp = temp;
        this.condition = condition;
        this.city = city;
        this.icon = icon;
    }

    public WeatherDataModel(JSONObject jsonObject) throws JSONException {
        this();

        this.condition = jsonObject.getJSONArray("weather")
                .getJSONObject(0).getInt("id");
        this.icon = updateWeatherIcon(this.condition);
        double temp = (jsonObject.getJSONObject("main").getDouble("temp") - 273.15);
        // * 9.0 / 5.0 + 32
        int roundedValue = (int) Math.rint(temp);
        this.temp = Integer.toString(roundedValue);
        this.city = jsonObject.getString("name");
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public int getCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

// TODO: Create a WeatherDataModel from a JSON:


    private static String updateWeatherIcon(int condition) {
        if (condition >= 0 && condition < 300) {
            return "tstorm1";
        } else if (condition >= 300 && condition < 500) {
            return "light_rain";
        } else if (condition >= 500 && condition < 600) {
            return "shower3";
        } else if (condition >= 600 && condition <= 700) {
            return "snow4";
        } else if (condition >= 701 && condition <= 771) {
            return "fog";
        } else if (condition >= 772 && condition < 800) {
            return "tstorm3";
        } else if (condition == 800) {
            return "sunny";
        } else if (condition >= 801 && condition <= 804) {
            return "cloudy2";
        } else if (condition >= 900 && condition <= 902) {
            return "tstorm3";
        } else if (condition == 903) {
            return "snow5";
        } else if (condition == 904) {
            return "sunny";
        } else if (condition >= 905 && condition <= 1000) {
            return "tstorm3";
        }
        return "dunno";
    }

    // TODO: Create getter methods for temperature, city, and icon name:


}
