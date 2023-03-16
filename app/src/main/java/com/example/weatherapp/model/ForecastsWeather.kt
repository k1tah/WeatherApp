package com.example.weatherapp.model

import com.squareup.moshi.Json

data class ForecastsWeather(
    @Json(name = "forecastday") val listForecasts: List<DayWeather>
)

