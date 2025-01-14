package com.example.movies.todo.data.remote

import com.example.movies.todo.data.Item

data class ItemEvent(val type: String, val payload: Item)
