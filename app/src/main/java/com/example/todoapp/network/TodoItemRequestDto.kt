package com.example.todoapp.network

import com.google.gson.annotations.SerializedName

data class TodoItemRequestDto(
    @SerializedName("element")
    val element: TodoItemDto
)