package com.example.todoapp.network

import com.google.gson.annotations.SerializedName

data class TodoListRequestDto(
    @SerializedName("list")
    val list: List<TodoItemDto>
)