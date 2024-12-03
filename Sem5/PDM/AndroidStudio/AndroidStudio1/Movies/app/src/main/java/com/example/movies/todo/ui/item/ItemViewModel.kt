package com.example.movies.todo.ui.item

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.movies.MyApplication
import com.example.movies.core.Result
import com.example.movies.core.TAG
import com.example.movies.todo.data.Item
import com.example.movies.todo.data.ItemRepository
import kotlinx.coroutines.launch
import java.util.Date

data class ItemUiState(
    val itemId: String? = null,
    val item: Item = Item(),
    var loadResult: Result<Item>? = null,
    var submitResult: Result<Item>? = null,
)

class ItemViewModel(private val itemId: String?, private val itemRepository: ItemRepository) :
    ViewModel() {

    var uiState: ItemUiState by mutableStateOf(ItemUiState(loadResult = Result.Loading))
        private set

    init {
        Log.d(TAG, "init")
        if (itemId != null) {
            loadItem()
        } else {
            uiState = uiState.copy(loadResult = Result.Success(Item()))
        }
    }

    fun loadItem() {
        viewModelScope.launch {
            itemRepository.itemStream.collect { items ->
                if (uiState.loadResult !is Result.Loading) {
                    return@collect
                }
                val item = items.find { it._id == itemId } ?: Item()
                uiState = uiState.copy(item = item, loadResult = Result.Success(item))
            }
        }
    }


    fun saveOrUpdateItem(title: String, savedDate: String, rating: Int, isViewed: Boolean) {
        viewModelScope.launch {
            Log.d(TAG, "saveOrUpdateItem...");
            try {
                uiState = uiState.copy(submitResult = Result.Loading)
                val item = uiState.item.copy(
                    title = title, savedDate = savedDate, rating = rating, isViewed = isViewed
                )
                val savedItem: Item
                if (itemId == null || itemId == "") {
                    savedItem = itemRepository.save(item)
                } else {
                    savedItem = itemRepository.update(item)
                }
                Log.d(TAG, "saveOrUpdateItem succeeded");
                uiState = uiState.copy(submitResult = Result.Success(savedItem))
            } catch (e: Exception) {
                Log.d(TAG, "saveOrUpdateItem failed");
                Log.d(TAG, e.toString())
                uiState = uiState.copy(submitResult = Result.Error(e))
            }
        }
    }

    companion object {
        fun Factory(itemId: String?): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApplication)
                ItemViewModel(itemId, app.container.itemRepository)
            }
        }
    }
}
