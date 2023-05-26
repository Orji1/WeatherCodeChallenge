package com.chimezie_interview.weather.navigation

import android.annotation.SuppressLint
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chimezie_interview.weather.data.repository.WeatherDataStore
import com.chimezie_interview.weather.ui.dashboard.DashboardScreen


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AppNavigation(weatherDataStore: WeatherDataStore) {
    val navController = rememberNavController()
    val targetScreen = rememberSaveable { mutableStateOf(Screen.Dashboard) }

    Scaffold(// topBar = { MyAppTopBar(targetScreen.value, navController) },
        content = {
            NavHost(
                navController = navController, startDestination = Screen.Dashboard.route,
            ) {
                composable(Screen.Dashboard.route) {
                    targetScreen.value = Screen.Dashboard
                    DashboardScreen(weatherDataStore)
                }
            }
        })
}

@Composable
fun MyAppTopBar(targetScreen: Screen, navController: NavController) {
    if (targetScreen != Screen.Dashboard) {
        TopAppBar(
            title = { Text(text = targetScreen.header, color = Color.White) },
            navigationIcon = { BackButton(navController = navController) },
            backgroundColor = Color(0xFF6F7FF7)
        )
    } else {
        TopAppBar(
            title = { Text(text = targetScreen.header, color = Color.White) },
            backgroundColor = Color(0xFF6F7FF7)
        )
    }
}

@Composable
fun BackButton(navController: NavController) {
    IconButton(onClick = { navController.navigateUp() }) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "Back",
            tint = Color.White
        )
    }
}
