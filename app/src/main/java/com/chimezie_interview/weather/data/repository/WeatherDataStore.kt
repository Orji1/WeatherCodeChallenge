package com.chimezie_interview.weather.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.chimezie_interview.weather.data.model.StoredPreferences
import com.chimezie_interview.weather.data.model.TempMeasure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WeatherDataStore(context: Context) {
    val Context.datastore
            : DataStore<Preferences> by preferencesDataStore("WEATHER")
    private var pref = context.datastore

    companion object {
        val lastSearchedLocation = stringPreferencesKey("LAST_SEARCHED_LOCATION")
        val preferredTempMeasure = stringPreferencesKey("PREFERRED_TEMP_MEASURE")
    }

    suspend fun updatedLastSearchedLocation(searchedLocation: String) {
        pref.edit {
            it[lastSearchedLocation] = searchedLocation
        }
    }

    suspend fun updatedPreferredTempMeasure(tempMeasure: TempMeasure) {
        pref.edit {
            it[preferredTempMeasure] = tempMeasure.name
        }
    }

    fun getStoredPreferences(): Flow<StoredPreferences> =
        pref.data.map {
            StoredPreferences(it[lastSearchedLocation],
                it[preferredTempMeasure]?.let { t -> TempMeasure.valueOf(t) } ?: TempMeasure.Celcius)
        }

}