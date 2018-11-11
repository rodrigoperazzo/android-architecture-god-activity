package com.rperazzo.weatherapp.model.weather;

import com.rperazzo.weatherapp.model.weather.remote.WeatherRemote;
import com.rperazzo.weatherapp.ui.WeatherView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class WeatherRepositoryImplTest {

    WeatherRepositoryImpl weatherRepository;
    WeatherView mockView;
    WeatherRemote mockRemote;

    @Before
    public void setUp(){
        mockView = Mockito.mock(WeatherView.class);
        mockRemote = Mockito.mock(WeatherRemote.class);
        weatherRepository = new WeatherRepositoryImpl(mockView, mockRemote);
    }

    @Test
    public void doesNotCallRemoteIfTextIsNull() {
        weatherRepository.search(null, "metrics");
        Mockito.verify(mockRemote, Mockito.never())
                .find(null, "metrics", mockView);
    }

    @Test
    public void doesNotCallRemoteIfUnitsIsNull() {
        weatherRepository.search("recife", null);
        Mockito.verify(mockRemote, Mockito.never())
                .find("recife", null, mockView);
    }

    @Test
    public void callsRemoteWithValidParams() {
        weatherRepository.search("recife", "metrics");
        Mockito.verify(mockRemote)
                .find("recife", "metrics", mockView);
    }
}