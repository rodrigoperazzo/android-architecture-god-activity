package com.rperazzo.weatherapp.model.weather;

public interface WeatherRepository {
    void search(String text, String units);
}
