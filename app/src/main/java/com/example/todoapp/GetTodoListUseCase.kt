package com.example.todoapp

import androidx.lifecycle.LiveData
import com.example.todoapp.retrofit.TodoItem
import com.example.todoapp.retrofit.TodoListRepository

class GetTodoListUseCase(private val todoListRepository: TodoListRepository) {

    fun getTodoList(): LiveData<List<TodoItem>> = todoListRepository.getTodoList()
}