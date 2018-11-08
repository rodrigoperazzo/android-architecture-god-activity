package com.rperazzo.weatherapp;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class WeatherManager {

    private static final String API_URL =
            "http://api.openweathermap.org/data/2.5/";
    public static final String API_KEY =
            "520d6b47a12735bee8f69c57737d145f";

    public interface WeatherService {
        @GET("find")
        Call<FindResult> find(
                @Query("q") String cityName,
                @Query("units") String units,
                @Query("appid") String apiKey
        );
    }

    private static OkHttpClient mClient = new OkHttpClient();

    public static WeatherService getService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(mClient)
                .build();

        return retrofit.create(WeatherService.class);
    }

    public class FindResult {
        public final List<City> list;

        public FindResult(List<City> list) {
            this.list = list;
        }
    }

    public class City implements Serializable {

        public Integer id;
        public String name;
        public Sys sys;
        public Main main;
        public Wind wind;
        public Clouds clouds;
        public List<Weather> weather;

        public String getTitle() {
            return this.name + ", " + this.sys.country.toUpperCase();
        }

        public String getPressure() {
            return new DecimalFormat("#").format(this.main.pressure) + " hpa";
        }

        public String getWind() {
            return "wind " + new DecimalFormat("#.#").format(this.wind.speed);
        }

        public String getClouds() {
            return "clouds " + this.clouds.all + "%";
        }

        public String getTemperature() {
            return new DecimalFormat("#").format(this.main.temp);
        }

        public String getDescription() {
            return this.weather.get(0).description;
        }

        public class Sys implements Serializable {
            public String country;
        }

        public class Main implements Serializable {
            public double temp;
            public double pressure;
        }

        public class Wind implements Serializable {
            public double speed;
        }

        public class Clouds implements Serializable {
            public int all;
        }

        public class Weather implements Serializable {
            public String description;
            public String icon;
        }
    }
}


