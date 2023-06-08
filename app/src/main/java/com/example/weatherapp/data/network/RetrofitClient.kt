package com.example.weatherapp.data.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitClient {

    private const val GEO_BASE_URL = "https://api.mapbox.com/geocoding/v5/mapbox.places/"
    const val GEO_API_KEY =
        "ваш ключ"
    const val WEATHER_API_KEY = "ваш ключ"
    private const val WEATHER_BASE_URL =
        "https://api.weatherapi.com/v1/"

    private val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    val weatherService: WeatherService by lazy {
        getWeatherClient().create(WeatherService::class.java)
    }
    val geoService: GeoService by lazy {
        getGeoClient().create(GeoService::class.java)
    }

    private var weatherRetrofit: Retrofit? = null
    private var geoRetrofit: Retrofit? = null

    private fun getWeatherClient(): Retrofit {
        if (weatherRetrofit == null) {
            weatherRetrofit = Retrofit.Builder()
                .baseUrl(WEATHER_BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
        }
        return weatherRetrofit!!
    }

    private fun getGeoClient(): Retrofit {
        if (geoRetrofit == null) {
            geoRetrofit = Retrofit.Builder()
                .baseUrl(GEO_BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
        }
        return geoRetrofit!!
    }
}