package com.example.todoapp.retrofit

import androidx.lifecycle.LiveData
import retrofit2.http.GET

interface TodoListRepository {

    @GET("list")
    fun getTodoList(): LiveData<List<TodoItem>>



    fun getTodoItem(id: String): TodoItem




    fun editTodoItem(item: TodoItem)

    fun addTodoItem(item: TodoItem)

    fun deleteTodoItem(item: TodoItem)


}