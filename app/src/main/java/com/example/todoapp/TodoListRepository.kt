package com.example.todoapp

import kotlinx.coroutines.flow.Flow


interface TodoListRepository {

     fun getTodoList(): Flow<List<TodoItem>>

    // fun getTodoItem(id: String): TodoItem

    suspend fun editTodoItem(item: TodoItem)

    suspend fun addTodoItem(item: TodoItem)

    suspend fun deleteTodoItem(item: TodoItem, stringId:String)

    suspend fun deleteTodoItemWithoutPosition(item: TodoItem)

    suspend fun updateTodoList()

}