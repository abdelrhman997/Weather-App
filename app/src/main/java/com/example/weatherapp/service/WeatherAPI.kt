package com.example.weatherapp.service

import com.example.weatherapp.model.WeatherModel
import com.example.weatherapp.utils.Constants.apiKey
import com.example.weatherapp.utils.Constants.unitsValue
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    @GET("weather")
    suspend fun getData(
        @Query("q") cityName : String,
        @Query("appid") appid : String=apiKey,
        @Query("units") units : String=unitsValue

        ):Response<WeatherModel>
}
