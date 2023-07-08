package com.example.todoapp

import com.example.todoapp.network.TodoListRequestDto
import kotlinx.coroutines.flow.Flow


interface TodoListRepository {

     fun getTodoList(): Flow<List<TodoItem>>

    // fun getTodoItem(id: String): TodoItem

    suspend fun editTodoItem(item: TodoItem)

    suspend fun addTodoItem(item: TodoItem)

    suspend fun deleteTodoItem(item: TodoItem, stringId:String)

    suspend fun deleteTodoItemWithoutPosition(item: TodoItem)

    suspend fun updateTodoList()
    suspend fun deleteList()
    suspend fun updateTodoListFromInternet( revision: Int,
                                            body: TodoListRequestDto)

    ////возможно в другой реп
    suspend fun downloadTodoList():List<TodoItem>?
    suspend fun deleteTodoItemFromInternet(revision: Int,id:String)

    suspend fun addTodoItemToInternet(revision:Int,item: TodoItem)
    suspend fun updateListFromInternet(revision: Int, body: TodoListRequestDto)
}