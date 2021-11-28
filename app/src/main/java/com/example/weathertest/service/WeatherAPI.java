package com.example.weathertest.service;

import com.example.weathertest.model.WeatherModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {

   //

    //"https://api.nomics.com/v1/currencies/ticker?key=your-key-here&ids=BTC,ETH,XRP&interval=1d,30d&convert=EUR&per-page=100&page=1"
//https://api.openweathermap.org/data/2.5/forecast?q=Izmir&appid=61e8b0259c092b1b9a15474cd800ee25
    //q=Izmir
    @GET("forecast?")
    Call<WeatherModel> getData(@Query("q") String name,@Query("APPID") String app_id);

}
