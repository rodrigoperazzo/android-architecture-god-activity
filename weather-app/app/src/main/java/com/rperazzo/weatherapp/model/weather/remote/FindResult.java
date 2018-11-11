package com.rperazzo.weatherapp.model.weather.remote;

import com.rperazzo.weatherapp.model.weather.City;

import java.util.List;

public class FindResult {
    public final List<City> list;

    public FindResult(List<City> list) {
        this.list = list;
    }
}
