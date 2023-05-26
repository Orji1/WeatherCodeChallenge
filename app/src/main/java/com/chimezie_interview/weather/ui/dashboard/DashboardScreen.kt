package com.chimezie_interview.weather.ui.dashboard


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.chimezie_interview.weather.data.model.GeoModel
import com.chimezie_interview.weather.data.model.StoredPreferences
import com.chimezie_interview.weather.data.model.TempMeasure
import com.chimezie_interview.weather.data.model.WeatherCityModel
import com.chimezie_interview.weather.data.remote.ApiDetails
import com.chimezie_interview.weather.data.repository.WeatherDataStore
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.roundToInt

@Composable
@RequiresApi(Build.VERSION_CODES.O)
fun DashboardScreen(
    weatherDataStore: WeatherDataStore
) {

    val viewModel = hiltViewModel<DashboardViewModel>()
    //Retrieve the viewModels flows as states
    val weatherDetails: WeatherCityModel by viewModel.weatherDetails.collectAsState(initial = WeatherCityModel())
    val currentLocDetails: WeatherCityModel by viewModel.currentLocationDetails.collectAsState(initial = WeatherCityModel())
    val reportError: String by viewModel.reportError.collectAsState(initial = "")

    // Check and display if there is any error to be reported
    displayError(reportError, LocalContext.current) {viewModel.clearError() }

    //Retrieve the stored preferences
    val storedPreferences =
        weatherDataStore.getStoredPreferences().collectAsState(initial = StoredPreferences())
    val weatherSelectionState = remember { mutableStateOf(TempMeasure.Celcius) }

    //Load the last stored locations value and retrieve the temp as per the last selected temp Measure
    storedPreferences.value.lastStoredLocation?.let { lastLocation -> viewModel.loadWeatherDetails(lastLocation, {}) }
    weatherSelectionState.value = storedPreferences.value.tempMeasure
    val searchState = remember { mutableStateOf(TextFieldValue("")) }
    searchState.value = TextFieldValue(storedPreferences.value.lastStoredLocation?:"")

    val loadWeatherForSearch: (String) -> Unit =
        { searchString ->
            viewModel.loadWeatherDetails(searchString) {searchString -> weatherDataStore.updatedLastSearchedLocation(searchString)}
        }


    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()) {

        val locationGeoModel = remember { mutableStateOf(GeoModel()) }
        viewModel.loadWeatherDetails(locationGeoModel.value)

        LocationPermission(locationGeoModel = locationGeoModel)

        Spacer(modifier = Modifier.height(20.dp))
        // Place a search String holder
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()
            .padding(start = 10.dp,end = 10.dp)) {

            SearchText(searchState, loadWeatherForSearch)
        }

        // Place the Temp Measure selection
        Row {
            TempMeasureSelection(weatherSelectionState = weatherSelectionState, weatherDataStore)
        }


        Spacer(modifier = Modifier.height(10.dp))

        //Display Search Location Details
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            if (weatherDetails.name!!.isNotBlank()) {
                DashboardContent(weatherDetails, weatherSelectionState)
            }
        }

        //Display Current location if access is available
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            if (currentLocDetails.name!!.isNotBlank()) {
                DashboardContent(currentLocDetails, weatherSelectionState,)
            }
        }
    }

}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun DashboardContent(
    weatherDetails: WeatherCityModel, weatherSelectionState: MutableState<TempMeasure>
) {

        Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        val tempMeasure = weatherSelectionState.value

        Row(
            modifier = Modifier.padding(start = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row {

                    Text(
                        text = weatherDetails.name?.let { "$it (${weatherDetails.sys!!.country})" }
                            ?: "",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
                Row {
                    Column {
                        Text(
                            text = tempConverter(weatherDetails.main!!.temp, tempMeasure),
                            fontSize = 40.sp,
                            textAlign = TextAlign.Center
                        )

                        Text(text = "Timezone: ${applyTimeZone(weatherDetails.timezone!!)}")

                        val tempStat =
                            "${tempConverter(weatherDetails.main!!.tempMax, tempMeasure)}/${tempConverter(weatherDetails.main!!.tempMin, tempMeasure)}" +
                                    ", Feels like ${tempConverter(weatherDetails.main!!.feelsLike, tempMeasure)}"
                        Text(text = tempStat)

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = "Air Pressure: ${(weatherDetails.main!!.pressure).toString()} mb")

                        Text(text = "Humidity: ${(weatherDetails.main!!.humidity).toString()}%")
                        Text(text = "Visibility: ${(weatherDetails.visibility!! / 1000)} km")

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Sunrise: ${
                                toDisplayTime(weatherDetails.sys!!.sunrise!!.toLong() )
                            }"
                        )


                        Text(
                            text = "Sunset: ${
                                toDisplayTime(weatherDetails.sys!!.sunset!!.toLong() )
                            }"
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = "Wind Speed:  ${(weatherDetails.wind!!.speed)} km in ${weatherDetails.wind!!.deg}°")


                    }
                    Column {
                        val listState = rememberLazyListState()
                        LazyRow(state = listState) {
                            itemsIndexed(weatherDetails.weather ?: ArrayList()) { index, item ->
                                item?.let {
                                    AsyncImage(
                                        model = ApiDetails.iconUrl(it.icon!!),
                                        contentDescription = it.description,
                                        modifier = Modifier
                                            .size(180.dp)
                                    )
                                }
                            }
                        }
                    }
                }
                Row {
                    Column {
                        Spacer(modifier = Modifier.height(40.dp))
                    }
                }
            }
        }

    }
}


fun applyTimeZone(timeZone: Int): String {
    val fromGmt = timeZone.toDouble() / (60 * 60)
    return "${if (fromGmt > 0) "GMT +" else "GMT -"} ${Math.abs(fromGmt)}"
}

@RequiresApi(Build.VERSION_CODES.O)
fun toDisplayTime(time: Long): String =
    LocalDateTime.ofInstant(
        Instant.ofEpochMilli(time * 1000),
        TimeZone
            .getDefault().toZoneId()
    ).format(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss"))

fun tempConverter(temp: Double?, tempMeasure: TempMeasure): String {
    val defaultKelvin = 273.15
    val finalTemp = temp?: defaultKelvin
    return when (tempMeasure) {
        TempMeasure.Celcius -> finalTemp - defaultKelvin
        TempMeasure.Fahrenheit -> (finalTemp - defaultKelvin) * (9 / 5) + 32
        TempMeasure.Kelvin -> finalTemp
    }.roundToInt().toString() + "\u00B0 " + tempMeasure.symbol
}