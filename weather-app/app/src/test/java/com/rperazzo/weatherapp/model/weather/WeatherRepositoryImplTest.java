package com.rperazzo.weatherapp.model.weather;

import com.rperazzo.weatherapp.model.weather.remote.WeatherRemote;
import com.rperazzo.weatherapp.presentation.WeatherContract;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

public class WeatherRepositoryImplTest {

    WeatherRepositoryImpl weatherRepository;
    WeatherContract.Presenter mockPresenter;
    WeatherRemote mockRemote;

    @Before
    public void setUp(){
        mockPresenter = Mockito.mock(WeatherContract.Presenter.class);
        mockRemote = Mockito.mock(WeatherRemote.class);
        weatherRepository = new WeatherRepositoryImpl(mockRemote);
    }

    @Test
    public void doesNotCallRemoteIfTextIsNull() {
        weatherRepository.search(mockPresenter,null, "metrics");
        Mockito.verify(mockRemote, Mockito.never())
                .find(null, "metrics", mockPresenter);
    }

    @Test
    public void doesNotCallRemoteIfUnitsIsNull() {
        weatherRepository.search(mockPresenter,"recife", null);
        Mockito.verify(mockRemote, Mockito.never())
                .find("recife", null, mockPresenter);
    }

    @Test
    public void callsRemoteWithValidParams() {
        weatherRepository.search(mockPresenter,"recife", "metrics");
        Mockito.verify(mockRemote)
                .find("recife", "metrics", mockPresenter);
    }
}