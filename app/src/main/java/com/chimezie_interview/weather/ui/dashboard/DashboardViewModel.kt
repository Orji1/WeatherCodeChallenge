package com.chimezie_interview.weather.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chimezie_interview.weather.data.model.GeoModel
import com.chimezie_interview.weather.data.model.WeatherCityModel
import com.chimezie_interview.weather.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    val repository: Repository
) : ViewModel() {

    //Stateflow has to be initialized with the default/initial value
    private val _weatherDetails = MutableStateFlow(WeatherCityModel())
    val weatherDetails: StateFlow<WeatherCityModel> = _weatherDetails

    private val _currentLocationDetails = MutableStateFlow(WeatherCityModel())
    val currentLocationDetails: StateFlow<WeatherCityModel> = _currentLocationDetails

    private val _reportError: MutableStateFlow<String> = MutableStateFlow("")
    val reportError : StateFlow<String> = _reportError


    fun loadWeatherDetails(searchCity: String,  onSuccess: suspend (String) -> Unit) {
        viewModelScope.launch {
            try {
                _weatherDetails.value = repository.getWeather(searchCity)
                onSuccess(searchCity)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                _reportError.value = "Unable to get details for ${searchCity}: ${e.message}"
            }
        }
    }


    fun loadWeatherDetails(geoModel: GeoModel) {
        geoModel.lat?.let { lat ->
            geoModel.lon?.let { lon ->
                viewModelScope.launch {
                    try {
                        _currentLocationDetails.value = repository.getWeather(lat, lon)
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                        _reportError.value = "Unable to get details for ${geoModel}: ${e.message}"
                    }
                }
            }
        }
    }

    fun clearError() {
        _reportError.value = ""
    }
}
