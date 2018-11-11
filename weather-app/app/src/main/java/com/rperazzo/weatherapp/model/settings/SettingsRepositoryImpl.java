package com.rperazzo.weatherapp.model.settings;

import com.rperazzo.weatherapp.model.settings.local.SettingsLocal;

public class SettingsRepositoryImpl implements SettingsRepository {

    SettingsLocal mSettingsLocal;

    public SettingsRepositoryImpl(SettingsLocal local) {
        mSettingsLocal = local;
    }

    @Override
    public void setTemperatureUnit(String value) {
        mSettingsLocal.setTemperatureUnit(value);
    }

    @Override
    public String getTemperatureUnit() {
        return mSettingsLocal.getTemperatureUnit();
    }
}
