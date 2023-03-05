package com.example.weatherapp.model

import com.squareup.moshi.Json

data class Weather(
    @Json(name = "temp_avg") val temperature: Int,
    @Json(name = "condition") val condition: String
)
