package com.example.todoapp

import androidx.lifecycle.LiveData
import com.example.todoapp.retrofit.TodoItem
import kotlinx.coroutines.flow.Flow


interface TodoListRepository {

     fun getTodoList(): Flow<List<TodoItem>>

    suspend fun getTodoItem(id: String): TodoItem

    suspend fun editTodoItem(item: TodoItem)

    suspend fun addTodoItem(item: TodoItem)

    suspend fun deleteTodoItem(item: TodoItem)


}