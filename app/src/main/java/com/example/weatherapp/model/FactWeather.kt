package com.example.weatherapp.model

import com.squareup.moshi.Json

data class FactWeather(
    @Json(name = "temp") val temperature: Int,
    @Json(name = "condition") val condition: String
)
