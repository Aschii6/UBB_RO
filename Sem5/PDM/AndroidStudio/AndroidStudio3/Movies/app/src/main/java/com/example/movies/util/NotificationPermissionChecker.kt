package com.example.movies.util

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun NotificationPermissionChecker(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    val context = LocalContext.current
    var permissionGranted by remember { mutableStateOf(false) }
    var showPermissionButton by remember { mutableStateOf(false) }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            permissionGranted = true
            onPermissionGranted()
        } else {
            permissionGranted = false
            showPermissionButton = true
            onPermissionDenied()
        }
    }

    LaunchedEffect(key1 = true) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionStatus = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            )
            if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
                permissionGranted = true
                onPermissionGranted()
            } else {
                showPermissionButton = true
            }
        } else {
            permissionGranted = true
            onPermissionGranted()
        }
    }

    if (showPermissionButton && !permissionGranted) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Notification permission is required to show alerts.")
            Button(onClick = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }) {
                Text("Grant Permission")
            }
        }
    }
}

