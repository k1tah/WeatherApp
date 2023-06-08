package com.example.weatherapp.view


import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.R
import com.example.weatherapp.model.HourWeatherSpecs
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.utills.LoadingState
import com.example.weatherapp.viewModel.WeatherViewModel

@Composable
fun NowWeather(
    viewModel: WeatherViewModel = viewModel()
) {
    val temperature =
        viewModel.forecastAtSelectedPlace.observeAsState().value?.currentWeatherSpecs!!.temperature
    val condition =
        viewModel.forecastAtSelectedPlace.observeAsState().value?.currentWeatherSpecs!!.condition
    val loadingState = viewModel.forecastsLoadingState.observeAsState().value!!
    Weather(
        temperature = temperature,
        conditionCode = condition.conditionCode,
        date = "Now",
        loadingState,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun TomorrowWeather(
    viewModel: WeatherViewModel = viewModel()
) {
    val temperature =
        viewModel.forecastAtSelectedPlace.observeAsState().value?.forecastsWeather!!.listForecasts[1].day.temperature
    val condition =
        viewModel.forecastAtSelectedPlace.observeAsState().value?.forecastsWeather!!.listForecasts[1].day.condition
    val date =
        viewModel.forecastAtSelectedPlace.observeAsState().value?.forecastsWeather!!.listForecasts[1].date
    val loadingState = viewModel.forecastsLoadingState.observeAsState().value!!
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = date)
        Spacer(modifier = Modifier.size(8.dp))
        Weather(
            temperature = temperature,
            conditionCode = condition.conditionCode,
            date = "Tomorrow",
            loadingState,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun WeekWeather(
    viewModel: WeatherViewModel = viewModel()
) {
    val forecast =
        viewModel.forecastAtSelectedPlace.observeAsState().value?.forecastsWeather!!.listForecasts
    val listState = rememberLazyListState()
    var selected by remember {
        mutableStateOf("")
    }
    var showHourlyWeather by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when (viewModel.forecastsLoadingState.observeAsState().value!!) {
            LoadingState.Loading -> {
                CircularProgressIndicator()
            }
            LoadingState.Error -> {
                Icon(
                    painter = rememberVectorPainter(image = Icons.Rounded.Warning),
                    contentDescription = "error"
                )
            }
            LoadingState.Done -> {
                LazyRow(
                    state = listState,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(items = forecast) { item ->
                        Weather(
                            temperature = item.day.temperature,
                            conditionCode = item.day.condition.conditionCode,
                            date = item.date,
                            LoadingState.Done,
                            modifier = Modifier.selectable(
                                selected = item.date == selected,
                                onClick = {
                                    if (selected != item.date) {
                                        selected = item.date
                                        showHourlyWeather = true
                                    } else {
                                        showHourlyWeather = !showHourlyWeather
                                    }
                                }
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.size(32.dp))
                forecast.find { it.date == selected }?.hour?.let {
                    AnimatedVisibility(
                        visible = showHourlyWeather,
                        enter = slideInVertically() + expandVertically(),
                        exit = slideOutVertically() + shrinkVertically()
                    ) {
                        HourlyWeather(forecast = it)
                    }
                }

            }
        }
    }
}

@Composable
fun HourlyWeather(
    forecast: List<HourWeatherSpecs>,
    viewModel: WeatherViewModel = viewModel()
) {
    when (viewModel.forecastsLoadingState.observeAsState().value!!) {
        LoadingState.Loading -> {
            CircularProgressIndicator()
        }
        LoadingState.Error -> {
            Icon(
                painter = rememberVectorPainter(image = Icons.Rounded.Warning),
                contentDescription = "error"
            )
        }
        LoadingState.Done -> {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = "Hours:")
                Spacer(modifier = Modifier.size(16.dp))
                if (forecast.isEmpty()) {
                    Row {
                        Icon(
                            painter = rememberVectorPainter(image = Icons.Rounded.Warning),
                            contentDescription = "no data"
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(text = "No data")
                    }
                } else {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(items = forecast) { item ->
                            Weather(
                                temperature = item.temperature,
                                conditionCode = item.condition.conditionCode,
                                date = item.hour,
                                LoadingState.Done
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Weather(
    temperature: Double,
    conditionCode: Int,
    date: String,
    loadingState: LoadingState,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (loadingState) {
            LoadingState.Loading -> {
                CircularProgressIndicator()
            }
            LoadingState.Error -> {
                Icon(
                    painter = rememberVectorPainter(image = Icons.Rounded.Warning),
                    contentDescription = "error"
                )
            }
            LoadingState.Done -> {
                Text(
                    text = date,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Row(
                    modifier = modifier
                        .height(50.dp)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        painter = when (conditionCode) {
                            1000 -> painterResource(id = R.drawable.clear)
                            1003 -> painterResource(id = R.drawable.partly_cloudy)
                            1006 -> painterResource(id = R.drawable.cloudy)
                            1009 -> painterResource(id = R.drawable.overcast)
                            1030 -> painterResource(id = R.drawable.mist)
                            1063 -> painterResource(id = R.drawable.patchy_rain_possible)
                            1066 -> painterResource(id = R.drawable.patchy_snow_possible)
                            1069 -> painterResource(id = R.drawable.patchy_sleet_possible)
                            1072 -> painterResource(id = R.drawable.patchy_freezing_drizzle_possible)
                            1087 -> painterResource(id = R.drawable.thundery_outbreaks_possible)
                            1114 -> painterResource(id = R.drawable.blowing_snow)
                            1117 -> painterResource(id = R.drawable.blizzard)
                            1135 -> painterResource(id = R.drawable.fog)
                            1147 -> painterResource(id = R.drawable.freezing_fog)
                            1150 -> painterResource(id = R.drawable.patchy_light_drizzle)
                            1153 -> painterResource(id = R.drawable.light_drizzle)
                            1168 -> painterResource(id = R.drawable.freezing_drizzle)
                            1171 -> painterResource(id = R.drawable.heavy_freezing_drizzle)
                            1180 -> painterResource(id = R.drawable.patchy_light_rain)
                            1183 -> painterResource(id = R.drawable.light_rain)
                            1186 -> painterResource(id = R.drawable.moderate_rain_at_times)
                            1189 -> painterResource(id = R.drawable.moderate_rain)
                            1192 -> painterResource(id = R.drawable.heavy_rain_at_times)
                            1195 -> painterResource(id = R.drawable.heavy_rain)
                            1198 -> painterResource(id = R.drawable.light_freezing_rain)
                            1201 -> painterResource(id = R.drawable.moderate_or_heavy_freezing_rain)
                            1204 -> painterResource(id = R.drawable.light_sleet)
                            1207 -> painterResource(id = R.drawable.moderate_or_heavy_sleet)
                            1210 -> painterResource(id = R.drawable.patchy_light_snow)
                            1213 -> painterResource(id = R.drawable.light_snow)
                            1216 -> painterResource(id = R.drawable.patchy_moderate_snow)
                            1219 -> painterResource(id = R.drawable.moderate_snow)
                            1222 -> painterResource(id = R.drawable.patchy_heavy_snow)
                            1225 -> painterResource(id = R.drawable.heavy_snow)
                            1237 -> painterResource(id = R.drawable.ice_pellets)
                            1240 -> painterResource(id = R.drawable.light_rain_shower)
                            1243 -> painterResource(id = R.drawable.moderate_or_heavy_rain_shower)
                            1246 -> painterResource(id = R.drawable.torrential_rain_shower)
                            1249 -> painterResource(id = R.drawable.light_sleet_showers)
                            1252 -> painterResource(id = R.drawable.moderate_or_heavy_sleet_showers)
                            1255 -> painterResource(id = R.drawable.light_snow_showers)
                            1258 -> painterResource(id = R.drawable.moderate_or_heavy_snow_showers)
                            1261 -> painterResource(id = R.drawable.light_showers_of_ice_pellets)
                            1264 -> painterResource(id = R.drawable.moderate_or_heavy_showers_of_ice_pellets)
                            1273 -> painterResource(id = R.drawable.patchy_light_rain_with_thunder)
                            1276 -> painterResource(id = R.drawable.moderate_or_heavy_rain_with_thunder)
                            1279 -> painterResource(id = R.drawable.patchy_light_snow_with_thunder)
                            1282 -> painterResource(id = R.drawable.moderate_or_heavy_snow_with_thunder)
                            else -> painterResource(id = R.drawable.ic_baseline_cloud_24)
                        },
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    Text(text = if (temperature > 0) "+$temperature" else temperature.toString())
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WeekWeatherPreview() {
    WeatherAppTheme {
        Surface {
            WeekWeather()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NowWeatherPreview() {
    WeatherAppTheme {
        Surface {
            NowWeather()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TomorrowWeatherPreview() {
    WeatherAppTheme {
        Surface {
            TomorrowWeather()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherPreview() {
    WeatherAppTheme {
        Surface {
            Weather(0.0, 1000, "Now", LoadingState.Done)
        }
    }
}