package com.chimezie_interview.weather.ui.dashboard


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.chimezie_interview.weather.data.model.TempMeasure
import com.chimezie_interview.weather.data.repository.WeatherDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun TempMeasureSelection(weatherSelectionState: MutableState<TempMeasure>, weatherDataStore: WeatherDataStore) {

    Row(
       modifier = Modifier
           .fillMaxWidth()
           .padding(end = 20.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End
    ) {
        fun updateTempMeasure(tempMeasure: TempMeasure) {
            weatherSelectionState.value = tempMeasure
            CoroutineScope(Dispatchers.IO).launch {
                weatherDataStore.updatedPreferredTempMeasure(tempMeasure)
            }
        }

        ClickableText(
            text = AnnotatedString(text= TempMeasure.Celcius.symbol.toString()),
            onClick = {
                updateTempMeasure(TempMeasure.Celcius)
            }
        )
        Spacer(modifier = Modifier.width(10.dp))

        ClickableText(
            text = AnnotatedString(text= TempMeasure.Fahrenheit.symbol.toString()),
            onClick = {
                updateTempMeasure(TempMeasure.Fahrenheit)

            }
        )
        Spacer(modifier = Modifier.width(10.dp))

        ClickableText(
            text = AnnotatedString(text= TempMeasure.Kelvin.symbol.toString()),
            onClick = {
                updateTempMeasure(TempMeasure.Kelvin)
            }
        )

    }


}