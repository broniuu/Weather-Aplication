package com.example.weatheraplication;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JsonPlaceholderAPI {

    @GET("weather")
    Call<Weather> getWeather(
            @Query("q") String cityName,
            @Query("APPID") String appId,
            @Query("units") String units
    );
}
