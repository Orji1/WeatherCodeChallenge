package com.chimezie_interview.weather.data.repository

import com.chimezie_interview.weather.data.model.WeatherCityModel
import com.chimezie_interview.weather.data.remote.ApiRequest
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val apiRequest: ApiRequest
) : Repository {

    override suspend fun getWeather(inputSearchString: String): WeatherCityModel {
        return apiRequest.getWeather(inputSearchString)
    }

    override suspend fun getWeather(latitude: Double, longitude: Double): WeatherCityModel {
        return apiRequest.getWeather(latitude, longitude)
    }
}