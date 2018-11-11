package com.rperazzo.weatherapp.model.weather.remote;

import com.rperazzo.weatherapp.ui.WeatherView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherRemoteImpl implements WeatherRemote {

    WeatherService mWeatherService;

    public  WeatherRemoteImpl() {
        mWeatherService = WeatherManager.getService();
    }

    public void find(String text, String units, final WeatherView view) {

        final Call<FindResult> findCall = mWeatherService.find(text, units, WeatherManager.API_KEY);
        findCall.enqueue(new Callback<FindResult>() {
            @Override
            public void onResponse(Call<FindResult> call, Response<FindResult> response) {
                if (response != null) {
                    FindResult result = response.body();
                    if (result != null) {
                        view.onFinishLoading(result.list);
                    } else {
                        view.onFinishLoading(null);
                    }
                } else {
                    view.onFinishLoading(null);
                }
            }

            @Override
            public void onFailure(Call<FindResult> call, Throwable t) {
                view.onFinishLoadingWithError("error");
            }
        });
    }

}
