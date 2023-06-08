package com.example.weatherapp.data.network

import com.example.weatherapp.data.network.RetrofitClient.WEATHER_API_KEY
import com.example.weatherapp.model.Weather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
//Запросы к API погоды
interface WeatherService {
    //Получение данных о погоде
    @GET("forecast.json?")
    fun getWeather(
        @Query("q") coordinates: String,
        @Query("days") days: Int = 7,
        @Query("key") apiKey: String = WEATHER_API_KEY
    ): Call<Weather>
}