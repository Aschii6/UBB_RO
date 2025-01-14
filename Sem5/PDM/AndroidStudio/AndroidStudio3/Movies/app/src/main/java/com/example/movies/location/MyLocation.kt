package com.example.movies.location

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movies.util.RequirePermissions
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MyLocation(modifier: Modifier = Modifier) {
    RequirePermissions(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ),
        modifier = modifier
    ) {
        val context = LocalContext.current
        val isPermissionGranted = context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        Log.d("Permissions", "Fine location granted: $isPermissionGranted")

        ShowMyLocation(
            modifier = modifier
        )
    }
}

@Composable
fun ShowMyLocation(modifier: Modifier) {
    val myLocationViewModel = viewModel<MyLocationViewModel>(
        factory = MyLocationViewModel.Factory(
            LocalContext.current.applicationContext as Application
        )
    )

    val location = myLocationViewModel.uiState
    Log.d("ShowMyLocation", "Location: $location")
    if (location != null) {
        MyMap(location.latitude, location.longitude, modifier)
    } else {
        LinearProgressIndicator()
    }
}
