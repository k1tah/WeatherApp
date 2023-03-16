package com.example.weatherapp.model

import com.squareup.moshi.Json

data class Weather(
    @Json(name = "current") val currentWeatherSpecs: CurrentWeatherSpecs,
    @Json(name = "forecast") val forecastsWeather: ForecastsWeather
)
