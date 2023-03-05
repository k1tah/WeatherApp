package com.example.weatherapp.view

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.utills.Times
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TimeTabRow(
    tabs: List<Times>,
    pagerState: PagerState
) {
    val scope = rememberCoroutineScope()
    androidx.compose.material.TabRow(
        selectedTabIndex = pagerState.currentPage,
        indicator = {
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, it)
            )
        },
        backgroundColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) {
        tabs.forEachIndexed { index, times ->
            Tab(
                selected = pagerState.currentPage == index, onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
                text = { Text(text = times.time) }
            )
        }
    }
}


@OptIn(ExperimentalPagerApi::class)
@Preview(showBackground = true)
@Composable
fun TimesTabPreview() {
    val pages = listOf(Times.Now, Times.Week, Times.Tomorrow)
    val pagerState = rememberPagerState(pageCount = pages.size)
    WeatherAppTheme {
        Surface {
            TimeTabRow(
                pages, pagerState
            )
        }
    }
}