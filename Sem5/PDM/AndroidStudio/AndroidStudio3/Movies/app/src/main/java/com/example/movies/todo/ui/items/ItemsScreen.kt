package com.example.movies.todo.ui.items

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movies.R
import com.example.movies.location.MyLocation
import com.example.movies.sensor.DeviceSensors
import com.example.movies.sensor.ProximitySensor
import com.example.movies.ui.MyJobs
import com.example.movies.ui.MyNetworkStatus
import com.example.movies.ui.MyNotifications
import com.example.movies.ui.animations.MyFloatingActionButton
import com.example.movies.util.NotificationPermissionChecker
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemsScreen(onItemClick: (id: String?) -> Unit, onAddItem: () -> Unit, onLogout: () -> Unit) {
    var isEditing by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    suspend fun showEditMessage() {
        if (!isEditing) {
            isEditing = true
            delay(3000L)
            isEditing = false
        }
    }

    Log.d("ItemsScreen", "recompose")
    val itemsViewModel = viewModel<ItemsViewModel>(factory = ItemsViewModel.Factory)
    val itemsUiState by itemsViewModel.uiState.collectAsStateWithLifecycle(
        initialValue = listOf()
    )
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.items)) },
                actions = {
                    Button(onClick = onLogout) { Text("Logout") }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    Log.d("ItemsScreen", "add")
                    onAddItem()
                },
            ) { Icon(Icons.Rounded.Add, "Add") }
        }
    ) {
        Column {
            ItemList(
                itemList = itemsUiState,
                onItemClick = onItemClick,
                modifier = Modifier.padding(it)
            )
            Spacer(modifier = Modifier.padding(16.dp))

            MyFloatingActionButton(
                onClick = {
                    coroutineScope.launch { showEditMessage() }
                },
                isEditing = isEditing,
            )
            Spacer(modifier = Modifier.padding(16.dp))

            /*DeviceSensors(modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.padding(16.dp))*/

            ProximitySensor(modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.padding(16.dp))

            MyLocation()
        }
    }
}

@Preview
@Composable
fun PreviewItemsScreen() {
    ItemsScreen(onItemClick = {}, onAddItem = {}, onLogout = {})
}

@Composable
private fun EditMessage(shown: Boolean) {
    AnimatedVisibility(
        visible = shown,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> -fullHeight },
            animationSpec = tween(durationMillis = 150, easing = LinearOutSlowInEasing)
        ),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> -fullHeight },
            animationSpec = tween(durationMillis = 250, easing = FastOutLinearInEasing)
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.secondary,
            shadowElevation = 4.dp,
        ) {
            Text(
                text = "Wow ce animatie tare",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
