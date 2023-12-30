package com.example.weatherapp.service

import com.example.weatherapp.model.WeatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    @GET("weather")
    suspend fun getData(
        @Query("q") cityName : String,
        @Query("appid") appid : String="f621b5d8ff6724370bd43c45e7939fca",
        @Query("units") units : String="metric"

        ):Response<WeatherModel>
}
