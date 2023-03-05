package com.example.weatherapp.view


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.R
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.utills.LoadingState
import com.example.weatherapp.viewModel.WeatherViewModel

@Composable
fun NowWeather(
    viewModel: WeatherViewModel = viewModel()
) {
    val temperature =
        viewModel.forecastAtSelectedPlace.observeAsState().value?.factWeather!!.temperature
    val condition =
        viewModel.forecastAtSelectedPlace.observeAsState().value?.factWeather!!.condition
    Weather(
        temperature = temperature,
        condition = condition,
        date = "Now",
        LoadingState.Done,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun TomorrowWeather(
    viewModel: WeatherViewModel = viewModel()
) {
    val temperature =
        viewModel.forecastAtSelectedPlace.observeAsState().value?.forecastsWeather!![1].parts.dayWeather.temperature
    val condition =
        viewModel.forecastAtSelectedPlace.observeAsState().value?.forecastsWeather!![1].parts.dayWeather.condition
    val date =
        viewModel.forecastAtSelectedPlace.observeAsState().value?.forecastsWeather!![1].date
    Weather(
        temperature = temperature,
        condition = condition,
        date = date,
        LoadingState.Done,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun WeekWeather(
    viewModel: WeatherViewModel = viewModel(),
) {
    val forecast =
        viewModel.forecastAtSelectedPlace.observeAsState().value?.forecastsWeather!!.take(7)
    LazyRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(items = forecast) { item ->
            Weather(
                temperature = item.parts.dayWeather.temperature,
                condition = item.parts.dayWeather.condition,
                date = item.date,
                LoadingState.Done
            )
        }
    }
}

@Composable
fun Weather(
    temperature: Int,
    condition: String,
    date: String,
    loadingState: LoadingState,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when(loadingState){
            LoadingState.Loading -> {

            }
            LoadingState.Error -> {
                //Icon(painter = Icons.Rounded, contentDescription = "error")
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
                        painter = when (condition) {
                            "overcast" -> painterResource(id = R.drawable.c3)
                            "light-snow" -> painterResource(id = R.drawable.c3_s1)
                            "clear" -> painterResource(id = R.drawable.d)
                            "rain" -> painterResource(id = R.drawable.ovc_mra)
                            "partly-cloudy" -> painterResource(id = R.drawable.ic_baseline_cloud_24)
                            "cloudy" -> painterResource(id = R.drawable.bkn_d)
                            "drizzle" -> painterResource(id = R.drawable.ovc_ra)
                            "light_rain" -> painterResource(id = R.drawable.c3_r1)
                            "moderate-rain" -> painterResource(id = R.drawable.c3_r2)
                            "heavy_rain" -> painterResource(id = R.drawable.c3_r3)
                            "continuous-heavy-rain" -> painterResource(id = R.drawable.c3_r3)
                            "showers" -> painterResource(id = R.drawable.c3_r3)
                            "wet-snow" -> painterResource(id = R.drawable.c3_rs1)
                            "snow" -> painterResource(id = R.drawable.c3_s2)
                            "snow-showers" -> painterResource(id = R.drawable.c3_s3)
                            "hail" -> painterResource(id = R.drawable.hail)
                            "thunderstorm" -> painterResource(id = R.drawable.c3_st)
                            "thunderstorm-with-rain" -> painterResource(id = R.drawable.c3_r2_st)
                            "thunderstorm-with-hail" -> painterResource(id = R.drawable.c3_s2_st)
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
            Weather(0, "cloudy", "Now", LoadingState.Done)
        }
    }
}