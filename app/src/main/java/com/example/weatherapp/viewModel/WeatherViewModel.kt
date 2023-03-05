package com.example.weatherapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.network.RetrofitClient.geoService
import com.example.weatherapp.data.network.RetrofitClient.weatherService
import com.example.weatherapp.model.*
import com.example.weatherapp.utills.LoadingState
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherViewModel : ViewModel() {

    private val _forecastsLoadingStates: MutableLiveData<LoadingState> =
        MutableLiveData(LoadingState.Loading)
    val forecastsLoadingStates: LiveData<LoadingState> get() = _forecastsLoadingStates

    val selectedPlace: MutableLiveData<Place> = MutableLiveData(
        Place(
            "Moscow",
            listOf(
                37.61f,
                55.75f
            )
        )
    )
    //private val _defaultPlace: MutableLiveData<Place> = MutableLiveData()

    //val defaultPlace: LiveData<Place> get() = _defaultPlace

    private val _forecastAtSelectedPlace: MutableLiveData<Forecasts> = MutableLiveData(
        Forecasts(
            FactWeather(
                0, "cloudy"
            ),
            listOf(
                ForecastsWeather("2016-08-03", Parts(Weather(0, "cloudy"))),
                ForecastsWeather("2016-08-03", Parts(Weather(0, "cloudy"))),
                ForecastsWeather("2016-08-03", Parts(Weather(0, "cloudy"))),
                ForecastsWeather("2016-08-03", Parts(Weather(0, "cloudy"))),
                ForecastsWeather("2016-08-03", Parts(Weather(0, "cloudy"))),
                ForecastsWeather("2016-08-03", Parts(Weather(0, "cloudy"))),
                ForecastsWeather("2016-08-03", Parts(Weather(0, "cloudy")))
            )
        )
    )

    val forecastAtSelectedPlace: LiveData<Forecasts> get() = _forecastAtSelectedPlace

    private val _listQueryPlaces: MutableLiveData<List<Place>> = MutableLiveData()
    val listQueryPlaces: LiveData<List<Place>> get() = _listQueryPlaces

    fun searchPlaces(query: String, onFailure: (String) -> Unit) {
        val filteredList = mutableListOf<Place>()
        geoService.getListPlaces(query).enqueue(object : Callback<PlacesResponse> {
            override fun onResponse(
                call: Call<PlacesResponse>,
                response: Response<PlacesResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.responseList?.forEach {
                        if (it.name.lowercase().contains(query.lowercase())) {
                            filteredList.add(it)
                        }
                        _listQueryPlaces.value = filteredList
                    }
                } else {
                    Log.d("TAG", "ne sus")
                }
            }

            override fun onFailure(call: Call<PlacesResponse>, t: Throwable) {
                Log.d("TAG", t.message.toString())
                onFailure(t.message.toString())
            }

        })
    }

    fun getWeatherInSelectedPlace(place: Place, onFailure: (String) -> Unit) {
        viewModelScope.launch {
            weatherService.getWeather(
                latitude = place.coordinates[1].toString(),
                longitude = place.coordinates[0].toString()
            )
                .enqueue(object : Callback<Forecasts> {
                    override fun onResponse(call: Call<Forecasts>, response: Response<Forecasts>) {
                        if (response.isSuccessful) {
                            _forecastAtSelectedPlace.value = response.body()
                            _forecastsLoadingStates.value = LoadingState.Done
                        }
                    }

                    override fun onFailure(call: Call<Forecasts>, t: Throwable) {
                        _forecastsLoadingStates.value = LoadingState.Error
                        Log.d("TAG", t.message.toString())
                        onFailure(t.message.toString())
                    }
                })
        }
    }

    fun getCurrentPlace(longitude: Float, latitude: Float) {
        geoService.getPlace(longitude, latitude).enqueue(object : Callback<PlacesResponse> {
            override fun onResponse(
                call: Call<PlacesResponse>,
                response: Response<PlacesResponse>
            ) {
                if (response.isSuccessful) {
                    selectedPlace.value = response.body()?.responseList?.get(0)
                } else {
                    Log.d("TAG", "ne sus")
                }
            }

            override fun onFailure(call: Call<PlacesResponse>, t: Throwable) {
                Log.d("TAG", t.message.toString())
            }
        })
    }


}