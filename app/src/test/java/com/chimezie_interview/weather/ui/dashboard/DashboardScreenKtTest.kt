package com.chimezie_interview.weather.ui.dashboard

import com.chimezie_interview.weather.data.model.TempMeasure
import org.junit.Assert.*
import org.junit.Test

class DashboardScreenKtTest {

    @Test
    fun testApplyTimeZone() {
        val gmtPlus2 = 7200
        assertEquals("GMT + 2.0", applyTimeZone(gmtPlus2))

        val gmtMinus2 = -7200
        assertEquals("GMT - 2.0", applyTimeZone(gmtMinus2))
    }

    @Test
    fun testEpocDisplayTime() {
        val time1 = 1684985240L
        val time2 = 1685042238L

        assertEquals("25-May-2023 04:27:20", toDisplayTime(time1))
        assertEquals("25-May-2023 20:17:18", toDisplayTime(time2))
    }

    @Test
    fun testTempKelvinToKelvin() {
        val kelvin = 293.47
        assertEquals("293° K", tempConverter(kelvin, TempMeasure.Kelvin))
        assertEquals("273° K", tempConverter(null, TempMeasure.Kelvin))
    }

    @Test
    fun testTempKelvinToCelsius() {
        val kelvin = 293.47
        assertEquals("20° C", tempConverter(kelvin, TempMeasure.Celcius))
        assertEquals("0° C", tempConverter(null, TempMeasure.Celcius))

    }

    @Test
    fun testTempKelvinToFahrenheit() {
        val kelvin = 293.47
        assertEquals("52° F", tempConverter(kelvin, TempMeasure.Fahrenheit))
        assertEquals("32° F", tempConverter(null, TempMeasure.Fahrenheit))

    }
}