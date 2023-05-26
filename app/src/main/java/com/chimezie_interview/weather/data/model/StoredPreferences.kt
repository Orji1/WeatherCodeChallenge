package com.chimezie_interview.weather.data.model

data class StoredPreferences(val lastStoredLocation: String? = null, val tempMeasure: TempMeasure = TempMeasure.Celcius)