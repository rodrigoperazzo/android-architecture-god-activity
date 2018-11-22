package com.rperazzo.weatherapp.presentation;

import com.rperazzo.weatherapp.model.weather.City;

import java.util.List;

public interface WeatherContract {

    interface Presenter {
        void onAttachView(View view);
        void onDettachView();
        void onSearchClick(String searchText);
        void onUnitClick(String unitClicked, String currentText);
        void onFinishSearching(List<City> list);
        void onFinishSearchingWithError(String error);
    }

    interface View {
        void onStartLoading();
        void onFinishLoading(List<City> list, String units);
        void onFinishLoadingWithError(String error);
    }
}
