package com.rperazzo.weatherapp.model.weather.remote;

import com.rperazzo.weatherapp.presentation.WeatherContract;

public interface WeatherRemote {
    void find(String text, String units, final WeatherContract.Presenter presenter);
}
