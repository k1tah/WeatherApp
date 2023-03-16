package com.example.weatherapp.model

import com.squareup.moshi.Json

data class DayForecastWeatherSpecs(
    @Json(name = "avgtemp_c") val temperature: Double,
    @Json(name = "condition") val condition: Condition
)
data class HourWeatherSpecs(
    @Json(name = "time") val time: String,
    @Json(name = "temp_c") val temperature: Double,
    @Json(name = "condition") val condition: Condition
){
    val hour get() = time.substringAfter(" ")
}
data class CurrentWeatherSpecs(
    @Json(name = "temp_c") val temperature: Double,
    @Json(name = "condition") val condition: Condition
)
