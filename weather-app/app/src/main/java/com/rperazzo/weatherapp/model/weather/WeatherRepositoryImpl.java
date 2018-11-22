package com.rperazzo.weatherapp.model.weather;

import com.rperazzo.weatherapp.model.weather.remote.WeatherRemote;
import com.rperazzo.weatherapp.presentation.WeatherContract;

public class WeatherRepositoryImpl implements WeatherRepository {

    WeatherRemote mRemote;

    public WeatherRepositoryImpl(WeatherRemote remote) {
        mRemote = remote;
    }

    @Override
    public void search(WeatherContract.Presenter presenter, String text, String units) {

        if (text != null && !text.isEmpty() && units != null && !units.isEmpty()) {
            mRemote.find(text, units, presenter);
        }
    }
}
