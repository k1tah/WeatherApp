package com.example.weatherapp

import android.content.res.Configuration
import android.os.Bundle
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.utills.LoadingState
import com.example.weatherapp.utills.Times
import com.example.weatherapp.utills.checkPermission
import com.example.weatherapp.utills.showShortToast
import com.example.weatherapp.view.DialogWindow
import com.example.weatherapp.view.FindPlace
import com.example.weatherapp.view.TimeTabRow
import com.example.weatherapp.viewModel.WeatherViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.android.gms.location.*

private lateinit var fusedLocationClient: FusedLocationProviderClient
private lateinit var locationRequest: LocationRequest

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPagerApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //location settings
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
            100
        ).build()

        setContent {
            WeatherAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    HomeScreen()
                }
            }
        }
    }
}


@Composable
@ExperimentalPagerApi
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModel = viewModel()
) {
    var showDefaultLocation by rememberSaveable {
        mutableStateOf(true)
    }
    //location permission
    val context = LocalContext.current
    var showDialogWindow by rememberSaveable {
        mutableStateOf(!checkPermission(context))
    }
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            showShortToast(context, "Permission not granted")
        }
    }
    //get location
    val place = viewModel.selectedPlace.observeAsState().value
    val locationCallback = remember {
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.lastLocation ?: return
                viewModel.getCurrentPlace(
                    locationResult.lastLocation!!.longitude.toFloat(),
                    locationResult.lastLocation!!.latitude.toFloat()
                )
            }
        }
    }
    /*val locationSettingsRequest =
        LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
    val settingsClient = LocationServices.getSettingsClient(context)
    val task = settingsClient.checkLocationSettings(locationSettingsRequest.build()).apply {
        addOnSuccessListener {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
        addOnFailureListener {
            *//*if (exception is ResolvableApiException){
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    exception.startResolutionForResult(context as Activity, 0)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }*//*
            Log.d("TAG", it.message.toString())
        }
    }*/
    //get location 2
    if (showDefaultLocation) {
        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (ex: SecurityException) {
            showShortToast(context, "Security Exception, no location available")
        }
    }

    val placeLoadingState = viewModel.placeLoadingState.observeAsState().value!!

    val pages = listOf(Times.Now, Times.Tomorrow, Times.Week)
    val selectedTime = rememberPagerState(pageCount = pages.size)


    Column(
        modifier = modifier.fillMaxSize().padding(vertical = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        FindPlace(onPlaceClicked = {
            showDefaultLocation = false
        })
        Spacer(modifier = Modifier.size(48.dp))
        TimeTabRow(
            pages,
            selectedTime
        )
        Spacer(modifier = Modifier.size(48.dp))
        AnimatedVisibility(visible = showDialogWindow) {
            DialogWindow(
                onAccept = {
                    showDialogWindow = false
                    requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_COARSE_LOCATION)
                }, onDecline = {
                    showDialogWindow = false
                    showShortToast(context, "Permission not granted")
                }, modifier = Modifier.fillMaxWidth()
            )
        }
        if (!showDialogWindow) {
            when (placeLoadingState) {
                LoadingState.Loading ->{
                    Text(text = "Location determination...")
                }
                LoadingState.Error ->{
                    viewModel.getWeatherInSelectedPlace(place!!) {
                        showShortToast(context, it)
                    }
                    Text(text = place.name)
                }
                LoadingState.Done -> {
                    Text(text = place!!.name)
                }
            }
            Spacer(modifier = Modifier.size(16.dp))
            HorizontalPager(
                state = selectedTime,
                dragEnabled = false,
                modifier = Modifier.fillMaxHeight()
            ) {
                pages[it].view()
            }
        }
    }

}


@OptIn(ExperimentalPagerApi::class)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenPreview() {
    WeatherAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            HomeScreen()
        }
    }
}

