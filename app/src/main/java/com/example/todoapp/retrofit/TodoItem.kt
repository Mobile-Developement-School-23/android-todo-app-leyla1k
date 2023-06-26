package com.example.todoapp.retrofit

import java.util.*

data class TodoItem(
    val id: String,
    var msg: String,
    var priority: ItemPriority,
    var deadline: Date?,
    var isCompleted: Boolean,
    val createDate: Date,
    var changedDate: Date?
)  {
    enum class ItemPriority {
        URGENT,
        NORMAL,
        LOW
    }
}