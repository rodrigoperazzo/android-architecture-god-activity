package com.rperazzo.weatherapp.model.settings.local;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsLocalImpl implements SettingsLocal {

    private static final String PREFERENCE_NAME = "com.rperazzo.weatherapp.shared";
    private static final String TEMPERATURE_UNIT_KEY = "TEMPERATURE_UNIT_KEY";

    private SharedPreferences mSharedPref;

    public SettingsLocalImpl(Context context) {
        mSharedPref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public void setTemperatureUnit(String value) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(TEMPERATURE_UNIT_KEY, value);
        editor.apply();
    }

    @Override
    public String getTemperatureUnit() {
        return mSharedPref.getString(TEMPERATURE_UNIT_KEY, "metric");
    }
}
