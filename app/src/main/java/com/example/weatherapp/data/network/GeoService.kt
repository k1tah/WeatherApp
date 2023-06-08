package com.example.weatherapp.data.network


import com.example.weatherapp.data.network.RetrofitClient.GEO_API_KEY
import com.example.weatherapp.model.Place
import com.example.weatherapp.model.PlacesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

//Гео запросы
interface GeoService {
    //Получение списка мест
    @GET("{query}.json?")
    fun getListPlaces(
        @Path("query") name: String,
        @Query("autocomplete") autoComplete: Boolean = true,
        @Query("language") language: String = "en",
        @Query("types") types: String = "place",
        @Query("access_token") apiKey: String = GEO_API_KEY
    ): Call<PlacesResponse>
    //Получение места
    @GET("{longitude},{latitude}.json?")
    fun getPlace(
        @Path("longitude") longitude: Float,
        @Path("latitude") latitude: Float,
        @Query("language") language: String = "en",
        @Query("types") types: String = "place",
        @Query("access_token") apiKey: String = GEO_API_KEY
    ): Call<PlacesResponse>
}