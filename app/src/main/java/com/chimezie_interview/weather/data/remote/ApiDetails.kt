package com.chimezie_interview.weather.data.remote

object ApiDetails {

    const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    const val WEATHER_URL = "weather"
    const val APP_ID = "df81ead564b00fb82a892624c58e13d8"
    const val WEATHER_ICON = "https://openweathermap.org/img/wn/{icon_id}@2x.png"
    fun iconUrl(icon: String) = WEATHER_ICON.replace("{icon_id}", icon)
}