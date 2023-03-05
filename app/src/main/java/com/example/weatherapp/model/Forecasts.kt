package com.example.weatherapp.model

import com.squareup.moshi.Json

data class Forecasts(
    @Json(name = "fact") val factWeather: FactWeather,
    @Json(name = "forecasts") val forecastsWeather: List<ForecastsWeather>
)

