package com.example.weatherapp.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.utills.clearFocusOnKeyboardDismiss
import com.example.weatherapp.utills.showShortToast
import com.example.weatherapp.viewModel.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindPlace(
    onPlaceClicked: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModel = viewModel()
) {
    val context = LocalContext.current
    val listPlaces = viewModel.listQueryPlaces.observeAsState().value
    var showTrailingIcon by remember { mutableStateOf(false) }
    var place by remember {
        mutableStateOf("")
    }
    OutlinedTextField(
        modifier = modifier.clearFocusOnKeyboardDismiss(),
        value = place,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        maxLines = 1,
        singleLine = true,
        shape = CircleShape,
        onValueChange = {
            place = it
            viewModel.searchPlaces(it) { t ->
                showShortToast(context, t)
            }
            showTrailingIcon = place.isNotEmpty()
        },
        placeholder = { Text(text = "Find a place") },
        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
        trailingIcon = {
            if (showTrailingIcon) {
                IconButton(onClick = {
                    place = ""
                    viewModel.searchPlaces(place) { t ->
                        showShortToast(context, t)
                    }
                    onPlaceClicked()
                    showTrailingIcon = false
                }) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = null)
                }
            }
        }
    )
    Spacer(modifier = Modifier.size(5.dp))
    AnimatedVisibility(showTrailingIcon) {
        LazyColumn(
            modifier = Modifier.height(128.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (listPlaces != null) {
                items(items = listPlaces) { item ->
                    Box(
                        modifier = Modifier
                            .border(
                                2.dp,
                                MaterialTheme.colorScheme.onBackground,
                                MaterialTheme.shapes.small
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = item.name,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                                .clickable {
                                    viewModel.selectedPlace.value = item
                                    viewModel.getWeatherInSelectedPlace(item) {
                                        showShortToast(context, it)
                                    }
                                }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = false)
@Composable
fun FindPlacePreview() {
    WeatherAppTheme {
        Surface {
            FindPlace(onPlaceClicked = {})
        }
    }
}