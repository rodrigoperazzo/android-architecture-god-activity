package com.rperazzo.weatherapp.presentation;

import com.rperazzo.weatherapp.model.settings.SettingsRepository;
import com.rperazzo.weatherapp.model.weather.City;
import com.rperazzo.weatherapp.model.weather.WeatherRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class WeatherPresenterTest {

    WeatherPresenter weatherPresenter;
    WeatherRepository mockWeatherRepository;
    SettingsRepository mockSettingsRepository;
    WeatherContract.View mockView;

    @Before
    public void setUp() {
        mockWeatherRepository = Mockito.mock(WeatherRepository.class);
        mockSettingsRepository = Mockito.mock(SettingsRepository.class);
        mockView = Mockito.mock(WeatherContract.View.class);
        weatherPresenter = new WeatherPresenter(mockWeatherRepository, mockSettingsRepository);
    }

    @Test
    public void doNotSearchIfViewNotAttachedOnSearchClick() {
        weatherPresenter.onSearchClick("city");
        Mockito.verify(mockWeatherRepository, Mockito.never())
                .search(Mockito.eq(weatherPresenter),Mockito.anyString(),Mockito.anyString());
    }

    @Test
    public void doNotSearchIfTextIsEmptyOnSearchClick() {
        weatherPresenter.onAttachView(mockView);
        weatherPresenter.onSearchClick("");
        Mockito.verify(mockWeatherRepository, Mockito.never())
                .search(Mockito.eq(weatherPresenter),Mockito.anyString(),Mockito.anyString());
    }

    @Test
    public void doNotSearchIfTextIsNullOnSearchClick() {
        weatherPresenter.onAttachView(mockView);
        weatherPresenter.onSearchClick(null);
        Mockito.verify(mockWeatherRepository, Mockito.never())
                .search(Mockito.eq(weatherPresenter),Mockito.anyString(),Mockito.anyString());
    }

    @Test
    public void searchIfTextIsValidOnSearchClick() {
        weatherPresenter.onAttachView(mockView);
        weatherPresenter.onSearchClick("city");
        Mockito.verify(mockWeatherRepository)
                .search(Mockito.eq(weatherPresenter),Mockito.eq("city"),Mockito.anyString());
    }

    @Test
    public void doNotUpdateUnitsIfTheSameOnUnitClick() {
        Mockito.doReturn("same").when(mockSettingsRepository).getTemperatureUnit();
        weatherPresenter.onUnitClick("same",null);
        Mockito.verify(mockSettingsRepository, Mockito.never())
                .setTemperatureUnit(Mockito.anyString());
    }

    @Test
    public void updateUnitsIfNotTheSameOnUnitClick() {
        Mockito.doReturn("NotTheSame").when(mockSettingsRepository).getTemperatureUnit();
        weatherPresenter.onUnitClick("Same",null);
        Mockito.verify(mockSettingsRepository)
                .setTemperatureUnit(Mockito.eq("Same"));
    }

    @Test
    public void callErrorIfListIsNullOnFinishSearching() {
        weatherPresenter.onAttachView(mockView);
        weatherPresenter.onFinishSearching(null);
        Mockito.verify(mockView, Mockito.never())
                .onFinishLoading(Mockito.anyListOf(City.class),Mockito.anyString());
        Mockito.verify(mockView)
                .onFinishLoadingWithError(Mockito.anyString());
    }

    @Test
    public void callErrorIfListIsEmptyOnFinishSearching() {
        weatherPresenter.onAttachView(mockView);
        weatherPresenter.onFinishSearching(new ArrayList<City>());
        Mockito.verify(mockView, Mockito.never())
                .onFinishLoading(Mockito.anyListOf(City.class),Mockito.anyString());
        Mockito.verify(mockView)
                .onFinishLoadingWithError(Mockito.anyString());
    }

    @Test
    public void finishLoadingIfListIsValidOnFinishSearching() {
        weatherPresenter.onAttachView(mockView);
        List<City> list = new ArrayList<>();
        list.add(Mockito.mock(City.class));
        weatherPresenter.onFinishSearching(list);
        Mockito.verify(mockView)
                .onFinishLoading(Mockito.eq(list),Mockito.anyString());
        Mockito.verify(mockView, Mockito.never())
                .onFinishLoadingWithError(Mockito.anyString());
    }
}