package com.example.movies.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.movies.util.NotificationPermissionChecker
import com.example.movies.util.createNotificationChannel
import com.example.movies.util.showSimpleNotification

@Composable
fun MyNotifications() {
    val context = LocalContext.current
    val channelId = "MyTestChannel"
    val notificationId = 0

    /*LaunchedEffect(Unit) {
        createNotificationChannel(channelId, context)
    }*/

    NotificationPermissionChecker(
        onPermissionGranted = {
            createNotificationChannel(channelId, context)
        },
        onPermissionDenied = {

        }
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(onClick = {
            showSimpleNotification(
                context,
                channelId,
                notificationId,
                "Simple notification",
                "This is a simple notification with default priority."
            )
        }, modifier = Modifier.padding(top = 16.dp)) {
            Text(text = "Simple Notification")
        }
    }
}
