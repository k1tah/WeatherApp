package com.example.weatherapp.model

import com.squareup.moshi.Json

data class Place(
    @Json(name = "place_name") val name: String,
    @Json(name = "center") val coordinates: List<Double>
)

data class PlacesResponse(
    @Json(name = "features") val responseList: List<Place>
)
