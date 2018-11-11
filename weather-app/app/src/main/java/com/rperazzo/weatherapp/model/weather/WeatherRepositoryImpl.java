package com.rperazzo.weatherapp.model.weather;

import com.rperazzo.weatherapp.model.weather.remote.WeatherRemote;
import com.rperazzo.weatherapp.ui.WeatherView;

public class WeatherRepositoryImpl implements WeatherRepository {

    WeatherView mView;
    WeatherRemote mRemote;

    public WeatherRepositoryImpl(WeatherView view, WeatherRemote remote) {
        mView = view;
        mRemote = remote;
    }

    @Override
    public void search(String text, String units) {

        if (text != null && !text.isEmpty() && units != null && !units.isEmpty()) {

            text.toLowerCase()

            mRemote.find(text, units, mView);
        }
    }
}
