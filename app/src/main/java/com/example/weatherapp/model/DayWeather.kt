package com.example.weatherapp.model

import com.squareup.moshi.Json

data class DayWeather(
    @Json(name = "date") val date: String,
    @Json(name = "day") val day: DayForecastWeatherSpecs,
    @Json(name = "hour") val hour: List<HourWeatherSpecs>
)
