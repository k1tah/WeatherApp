package com.example.weatherapp.model

import com.squareup.moshi.Json

data class Parts(
    @Json(name = "day") val dayWeather: Weather
)
