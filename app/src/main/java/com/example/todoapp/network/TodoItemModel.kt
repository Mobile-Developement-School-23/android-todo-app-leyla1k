package com.example.todoapp.network

import java.util.*

data class TodoItemModel(
    val id: String,
    var msg: String,
    var priority: ItemPriority,
    var deadline: Date?,
    var isCompleted: Boolean,
    val createDate: Date,
    var changedDate: Date?
)

enum class ItemPriority(val value: Int) {
    URGENT(0),
    NORMAL(1),
    LOW(2)
}
