package com.rperazzo.weatherapp.model.weather.remote;

import com.rperazzo.weatherapp.ui.WeatherView;

public interface WeatherRemote {
    void find(String text, String units, final WeatherView view);
}
