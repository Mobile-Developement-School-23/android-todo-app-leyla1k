package com.example.todoapp.localbase

import com.example.todoapp.TodoItem

fun TodoItem.toDbModel(): TodoDataItem {


    return TodoDataItem(
        id = this.id.toLong(),
        msg = this.msg,
        priority = this.priority,
        deadline = this.deadline,
        isCompleted = this.isCompleted,
        createDate = this.createDate,
        changedDate =this.changedDate
    )
}

fun TodoDataItem.toEntity(): TodoItem {


    return TodoItem(
        id = this.id.toString(),
        msg = this.msg,
        priority = this.priority,
        deadline = this.deadline,
        isCompleted = this.isCompleted,
        createDate = this.createDate,
        changedDate = this.changedDate
    )

}

fun List<TodoDataItem>.toListOfToDoEntyty(): List<TodoItem> {
    return map { it.toEntity() }
}



