package com.rperazzo.weatherapp.model.weather;

import com.rperazzo.weatherapp.presentation.WeatherContract;

public interface WeatherRepository {
    void search(WeatherContract.Presenter presenter, String text, String units);
}
