package com.chimezie_interview.weather.data.remote


import com.chimezie_interview.weather.data.model.WeatherCityModel
import com.chimezie_interview.weather.data.remote.ApiDetails.APP_ID
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiRequest {

    @GET(ApiDetails.WEATHER_URL)
    suspend fun getWeather(@Query("q") searchParam: String, @Query("appid") appId: String = APP_ID): WeatherCityModel

    @GET(ApiDetails.WEATHER_URL)
    suspend fun getWeather(@Query("lat") latitude: Double, @Query("lon") longitude: Double, @Query("appid") appId: String = APP_ID): WeatherCityModel

}