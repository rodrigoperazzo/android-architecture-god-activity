package com.rperazzo.weatherapp.model.weather;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

public class City implements Serializable {

    public Integer id;
    public String name;
    public Sys sys;
    public Main main;
    public Wind wind;
    public Clouds clouds;
    public List<Weather> weather;

    public String getTitle() {
        return this.name + ", " + this.sys.country.toUpperCase();
    }

    public String getPressure() {
        return new DecimalFormat("#").format(this.main.pressure) + " hpa";
    }

    public String getWind() {
        return "wind " + new DecimalFormat("#.#").format(this.wind.speed);
    }

    public String getClouds() {
        return "clouds " + this.clouds.all + "%";
    }

    public String getTemperature() {
        return new DecimalFormat("#").format(this.main.temp);
    }

    public String getDescription() {
        return this.weather.get(0).description;
    }

    public class Sys implements Serializable {
        public String country;
    }

    public class Main implements Serializable {
        public double temp;
        public double pressure;
    }

    public class Wind implements Serializable {
        public double speed;
    }

    public class Clouds implements Serializable {
        public int all;
    }

    public class Weather implements Serializable {
        public String description;
        public String icon;
    }
}
