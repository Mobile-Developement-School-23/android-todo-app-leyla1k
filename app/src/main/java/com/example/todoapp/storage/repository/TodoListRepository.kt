package com.example.todoapp.storage.repository

import com.example.todoapp.TodoItem
import com.example.todoapp.network.TodoListRequestDto
import kotlinx.coroutines.flow.Flow


interface TodoListRepository {
    suspend fun createRevision()
     fun getTodoList(): Flow<List<TodoItem>>

    // fun getTodoItem(id: String): TodoItem

    suspend fun editTodoItem(item: TodoItem)

    suspend fun addTodoItem(item: TodoItem)

    suspend fun deleteTodoItem(item: TodoItem, stringId:String)
/*    fun insertRevision()*/
    suspend fun deleteTodoItemWithoutPosition(item: TodoItem)

    /*suspend fun updateTodoList()*/
    suspend fun deleteList()
    suspend fun updateTodoListFromInternet( revision: Int,
                                            body: TodoListRequestDto)
    suspend fun refreshData()
    suspend fun downloadTodoList():List<TodoItem>?
    suspend fun deleteTodoItemFromInternet(id:String)

    suspend fun addTodoItemToInternet(item: TodoItem)
    suspend fun updateListFromInternet(revision: Int, body: TodoListRequestDto)
    suspend fun editTodoItemToInternet(newItem: TodoItem)
    suspend fun getItemByID(id: String): TodoItem
}