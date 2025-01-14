package com.example.movies.util

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/*class LocationMonitor(val context: Context) {
    @SuppressLint("MissingPermission")
    val currentLocation: Flow<Location> = callbackFlow<Location> {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        fusedLocationClient.lastLocation.addOnSuccessListener {
            Log.d("LocationMonitor", "lastLocation $it")
            if (it != null) {
                channel.trySend(it)
            }
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    Log.d("LocationMonitor", "onLocationResult $location")
                    channel.trySend(location)
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(
            LocationRequest.Builder(10000).build(),
            locationCallback,
            Looper.getMainLooper()
        )

        awaitClose {
            Log.d("LocationMonitor", "removeLocationUpdates")
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }
}*/

class LocationMonitor(private val context: Context) {

    @SuppressLint("MissingPermission")
    val currentLocation: Flow<Location> = callbackFlow {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        // Attempt to fetch last known location
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            Log.d("LocationMonitor", "lastLocation: $location")
            if (location != null) {
                trySend(location).onFailure {
                    Log.e("LocationMonitor", "Failed to send last location: ${it?.message}")
                }
            } else {
                Log.d("LocationMonitor", "lastLocation is null, waiting for updates")
            }
        }.addOnFailureListener {
            Log.e("LocationMonitor", "Error fetching last location: ${it.message ?: "Unknown error"}")
        }

        // Set up location updates
        val locationRequest = LocationRequest.Builder(10000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setMinUpdateIntervalMillis(5000)
            .build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult ?: return
                Log.d("LocationMonitor", "Received location result: ${locationResult.locations}")
                locationResult.locations.forEach { location ->
                    trySend(location).onFailure {
                        Log.e("LocationMonitor", "Failed to send location: ${it?.message ?: "Unknown error"}")
                    }
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )

        // Clean up
        awaitClose {
            Log.d("LocationMonitor", "removeLocationUpdates")
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }
}
