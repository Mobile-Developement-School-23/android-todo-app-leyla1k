package com.example.todoapp.network

import android.os.Build
import com.example.todoapp.ItemPriority
import com.example.todoapp.TodoItem
import java.util.*
import java.util.UUID.fromString

fun mapDtoToEntity(todoItem: TodoItemDto) = TodoItem(
        id = todoItem.id.toString(),
        msg = todoItem.msg,
        priority = mapStringToPriority(todoItem.priority),
        deadline = mapLongToDate(todoItem.deadline),
        isCompleted = todoItem.isCompleted,
        createDate = mapLongToDate(todoItem.createDate)!!,
        changedDate = mapLongToDate(todoItem.changedDate)
    )


fun mapEntityToItemRequestDto(item: TodoItem) = TodoItemRequestDto(
    element = mapEntityToDto(item)
)




    fun mapEntityToDto(todoItem: TodoItem) = TodoItemDto(
        id = UUID.fromString(todoItem.id),
        //id =  UUID.nameUUIDFromBytes(todoItem.id.toByteArray()),
        msg = todoItem.msg,
        priority = mapPriorityToString(todoItem.priority),
        deadline = mapDateToLong(todoItem.deadline),
        isCompleted = todoItem.isCompleted,
        color = null,
        createDate = mapDateToLong(todoItem.createDate)?: 0L,
        changedDate = mapDateToLong(todoItem.changedDate)?: 0L,
        device = Build.DEVICE
    )

    fun mapListDtoToListEntity(list: List<TodoItemDto>) = list.map {
        mapDtoToEntity(it)
    }

   /* fun mapListEntityToListRequestDto(list: List<TodoItem>) = TodoListRequestDto(
        list = list.map {mapEntityToDto(it)}
    )

    fun mapEntityToItemRequestDto(item: TodoItem) = TodoItemRequestDto(
        element = mapEntityToDto(item)
    )*/

    private fun mapStringToPriority(priority: String) =
        when(priority){
            "low" -> ItemPriority.LOW
            "basic" -> ItemPriority.NORMAL
            "important" ->ItemPriority.URGENT
            else -> ItemPriority.NORMAL
        }

    private fun mapPriorityToString(priority: ItemPriority) =
        when(priority){
           ItemPriority.LOW -> "low"
           ItemPriority.NORMAL -> "basic"
           ItemPriority.URGENT -> "important"
        }


    private fun mapLongToDate(time: Long?): Date? {
        return if (time == null)
            null
        else {
            val date = Date()
            date.time = time
            date
        }
    }

    private fun mapDateToLong(date: Date?) = date?.time

