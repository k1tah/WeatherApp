package com.example.weatherapp.utills

import androidx.compose.runtime.Composable
import com.example.weatherapp.view.NowWeather
import com.example.weatherapp.view.TomorrowWeather
import com.example.weatherapp.view.WeekWeather


sealed class Times(
    val time: String,
    val view: @Composable () -> Unit
) {
    object Now : Times("Now", { NowWeather() })

    object Tomorrow : Times("Tomorrow", { TomorrowWeather() })

    object Week : Times("Week", { WeekWeather() })
}