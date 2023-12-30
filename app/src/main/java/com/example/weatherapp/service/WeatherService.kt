package com.example.weatherapp.service

import com.example.weatherapp.model.WeatherModel
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class WeatherService {
    private val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(WeatherAPI::class.java)

   suspend fun getApi(cityName : String) : Response<WeatherModel>{
        return api.getData(cityName)
    }
}