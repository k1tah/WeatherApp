package com.example.weatherapp.model

import com.squareup.moshi.Json

data class Condition(
    @Json(name = "code") val conditionCode: Int
)

