package com.example.weatherapp.data.network

import com.example.weatherapp.data.network.RetrofitClient.WEATHER_API_KEY
import com.example.weatherapp.model.Forecasts
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface WeatherService {
    @GET("forecast?")
    @Headers("X-Yandex-API-Key:${WEATHER_API_KEY}")
    fun getWeather(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String
    ): Call<Forecasts>
}