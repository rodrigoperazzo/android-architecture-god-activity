package com.rperazzo.weatherapp.model.settings;

public interface SettingsRepository {
    void setTemperatureUnit(String value);
    String getTemperatureUnit();
}
