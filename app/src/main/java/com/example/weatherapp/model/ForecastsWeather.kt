package com.example.weatherapp.model

import com.squareup.moshi.Json

data class ForecastsWeather(
    @Json(name = "date") val date: String,
    @Json(name = "parts") val parts: Parts
)
