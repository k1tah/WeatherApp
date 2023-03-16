package com.example.weatherapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.network.RetrofitClient.geoService
import com.example.weatherapp.data.network.RetrofitClient.weatherService
import com.example.weatherapp.model.*
import com.example.weatherapp.utills.ExampleForecasts.forecasts
import com.example.weatherapp.utills.LoadingState
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherViewModel : ViewModel() {

    private val _forecastsLoadingState: MutableLiveData<LoadingState> =
        MutableLiveData(LoadingState.Loading)
    val forecastsLoadingState: LiveData<LoadingState> get() = _forecastsLoadingState

    private val _placeLoadingState: MutableLiveData<LoadingState> =
        MutableLiveData(LoadingState.Loading)
    val placeLoadingState: LiveData<LoadingState> get() = _placeLoadingState

    val selectedPlace: MutableLiveData<Place> = MutableLiveData(
        Place(
            "Moscow, Russia", listOf(
                37.61, 55.75
            )
        )
    )

    private val _forecastAtSelectedPlace: MutableLiveData<Weather> = MutableLiveData(
        Weather(
            CurrentWeatherSpecs(
                0.0, Condition(1000)
            ), forecasts
        )
    )

    val forecastAtSelectedPlace: LiveData<Weather> get() = _forecastAtSelectedPlace

    private val _listQueryPlaces: MutableLiveData<List<Place>> = MutableLiveData()
    val listQueryPlaces: LiveData<List<Place>> get() = _listQueryPlaces

    fun searchPlaces(query: String, onFailure: (String) -> Unit) {
        val filteredList = mutableListOf<Place>()
        geoService.getListPlaces(query).enqueue(object : Callback<PlacesResponse> {
            override fun onResponse(
                call: Call<PlacesResponse>, response: Response<PlacesResponse>
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
                coordinates = "${place.coordinates[1]},${place.coordinates[0]}"
            ).enqueue(object : Callback<Weather> {
                override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                    if (response.isSuccessful) {
                        _forecastAtSelectedPlace.value = response.body()
                        _forecastsLoadingState.value = LoadingState.Done
                    } else {
                        Log.d("TAG", "ne sus")
                        _forecastsLoadingState.value = LoadingState.Error
                    }
                }

                override fun onFailure(call: Call<Weather>, t: Throwable) {
                    _forecastsLoadingState.value = LoadingState.Error
                    Log.d("TAG", t.message.toString())
                    onFailure(t.message.toString())
                }
            })
        }
    }

    fun getCurrentPlace(longitude: Float, latitude: Float) {
        geoService.getPlace(longitude, latitude).enqueue(object : Callback<PlacesResponse> {
            override fun onResponse(
                call: Call<PlacesResponse>, response: Response<PlacesResponse>
            ) {
                if (response.isSuccessful) {
                    selectedPlace.value = response.body()?.responseList?.get(0)
                    _placeLoadingState.value = LoadingState.Done
                    getWeatherInSelectedPlace(selectedPlace.value!!) {
                        Log.d("TAG", it)
                    }
                } else {
                    Log.d("TAG", "ne sus")
                    _placeLoadingState.value = LoadingState.Error
                }
            }

            override fun onFailure(call: Call<PlacesResponse>, t: Throwable) {
                _placeLoadingState.value = LoadingState.Error
                Log.d("TAG", t.message.toString())
            }
        })
    }


}