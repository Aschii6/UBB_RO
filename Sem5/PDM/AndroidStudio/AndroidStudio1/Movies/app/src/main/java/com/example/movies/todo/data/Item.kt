package com.example.movies.todo.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity(tableName = "items")
data class Item(
    @PrimaryKey val _id: String = "",
    val title: String = "",
    val savedDate: String = getCurrentDate(),
    val rating: Int = 0,
    val isViewed: Boolean = false,
)

private fun getCurrentDate(): String {
    val calendar = Calendar.getInstance()

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    return "$year/${month + 1}/$day"
}