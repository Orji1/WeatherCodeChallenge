package com.chimezie_interview.weather.data.repository

import com.chimezie_interview.weather.data.model.WeatherCityModel


interface Repository {

    suspend fun getWeather(inputSearchString: String): WeatherCityModel

    suspend fun getWeather(latitude: Double, longitude: Double): WeatherCityModel
}