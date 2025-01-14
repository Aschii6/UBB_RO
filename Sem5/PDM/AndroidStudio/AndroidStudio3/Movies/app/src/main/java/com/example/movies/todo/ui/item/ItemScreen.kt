package com.example.movies.todo.ui.item

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CalendarLocale
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.movies.R
import com.example.movies.core.Result
import com.example.movies.todo.ui.item.ItemViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemScreen(itemId: String?, onClose: () -> Unit) {
    val itemViewModel = viewModel<ItemViewModel>(factory = ItemViewModel.Factory(itemId))
    val itemUiState = itemViewModel.uiState
    var title by rememberSaveable { mutableStateOf(itemUiState.item.title) }
    var savedDate by rememberSaveable { mutableStateOf(itemUiState.item.savedDate) }
    var rating by rememberSaveable { mutableIntStateOf(itemUiState.item.rating) }
    var isViewed by rememberSaveable { mutableStateOf(itemUiState.item.isViewed) }

    val localContext = LocalContext.current

    LaunchedEffect(itemUiState.submitResult) {
        Log.d("ItemScreen", "Submit = ${itemUiState.submitResult}");
        if (itemUiState.submitResult is Result.Success) {
            Log.d("ItemScreen", "Closing screen");
            onClose();
        }
    }

    var textInitialized by remember { mutableStateOf(itemId == null) }
    LaunchedEffect(itemId, itemUiState.loadResult) {
        Log.d("ItemScreen", "Text initialized = ${itemUiState.loadResult}");
        if (textInitialized) {
            return@LaunchedEffect
        }
        if (itemUiState.loadResult !is Result.Loading) {
            title = itemUiState.item.title
            savedDate = itemUiState.item.savedDate
            rating = itemUiState.item.rating
            isViewed = itemUiState.item.isViewed
            textInitialized = true
        }
    }

    Scaffold(topBar = {
        TopAppBar(title = { Text(text = stringResource(id = R.string.item)) }, actions = {
            Button(onClick = {
                Log.d("ItemScreen", "save item title = $title");
                itemViewModel.saveOrUpdateItem(title, savedDate, rating, isViewed)
            }) { Text("Save") }
        })
    }) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            if (itemUiState.loadResult is Result.Loading) {
                CircularProgressIndicator()
                return@Scaffold
            }
            if (itemUiState.submitResult is Result.Loading) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) { LinearProgressIndicator() }
            }
            if (itemUiState.loadResult is Result.Error) {
                Text(text = "Failed to load item - ${(itemUiState.loadResult as Result.Error).exception?.message}")
            }
            Column {
                TextField(
                    value = title,
                    onValueChange = { title = it }, label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth(),
                )
                Text("Saved date")
                ClickableText(text = AnnotatedString(savedDate), onClick = {
                    val calendar = Calendar.getInstance()

                    val year = calendar.get(Calendar.YEAR)
                    val month = calendar.get(Calendar.MONTH)
                    val day = calendar.get(Calendar.DAY_OF_MONTH)

                    val mDatePickerDialog = DatePickerDialog(
                        localContext, { _: DatePicker, cYear: Int, cMonth: Int, cDay: Int ->
                            savedDate = "$cYear/${cMonth + 1}/$cDay"
                        }, year, month, day
                    )
                    mDatePickerDialog.show()
                }, modifier = Modifier.fillMaxWidth())
                TextField(
                    value = rating.toString(),
                    onValueChange = {
                        it.toIntOrNull()?.let { validNumber ->
                            rating = validNumber
                        }
                    },
                    label = { Text("Rating") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isViewed,
                        onCheckedChange = { isViewed = it }
                    )
                    Text(text = "Viewed")
                }
            }
            if (itemUiState.submitResult is Result.Error) {
                Text(
                    text = "Failed to submit item - ${(itemUiState.submitResult as Result.Error).exception?.message}",
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}


@Preview
@Composable
fun PreviewItemScreen() {
    ItemScreen(itemId = "0", onClose = {})
}