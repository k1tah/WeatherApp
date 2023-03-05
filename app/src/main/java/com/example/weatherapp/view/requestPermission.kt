package com.example.weatherapp.view

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weatherapp.ui.theme.WeatherAppTheme

@Composable
fun DialogWindow(
    onAccept: () -> Unit,
    onDecline: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Text(
            text = "To automatically determine the location of the weather, you must have permission",
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.size(16.dp))
        Button(onClick = { onAccept() }) {
            Text(text = "Accept")
        }
        Spacer(modifier = Modifier.size(5.dp))
        Button(onClick = { onDecline() }) {
            Text(text = "Decline")
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DialogWindowPreview() {
    WeatherAppTheme {
        Surface {
            DialogWindow(onAccept = { }, onDecline = { })
        }
    }
}