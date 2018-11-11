package com.rperazzo.weatherapp.ui;

import com.rperazzo.weatherapp.model.weather.City;

import java.util.List;

public interface WeatherView {
    void onFinishLoading(List<City> list);
    void onFinishLoadingWithError(String error);
}
