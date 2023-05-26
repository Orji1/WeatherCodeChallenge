package com.chimezie_interview.weather.ui.dashboard

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.chimezie_interview.weather.data.model.GeoModel
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices

@Composable
fun LocationPermission(locationGeoModel: MutableState<GeoModel>) {
    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    val locationPermissionGranted = remember { mutableStateOf(false) }

    //Initialize the launcher with the call back to find out if the permission is granted or denied
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        locationPermissionGranted.value = isGranted
        if (!isGranted) {
           displayError("Cannot display weather from current location. Location Permission Denied", context ){}
        }
    }


    //Launch the permission request if the location permission is not available
    if (!locationPermissionGranted.value) {
        SideEffect {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    } else  {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener { location ->
                    locationGeoModel.value = GeoModel(location.latitude, location.longitude)
                }
        }
    }
}