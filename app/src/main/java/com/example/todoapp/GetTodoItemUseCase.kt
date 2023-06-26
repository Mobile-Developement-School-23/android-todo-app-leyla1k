package com.example.todoapp

import com.example.todoapp.retrofit.TodoItem
import com.example.todoapp.retrofit.TodoListRepository

class GetTodoItemUseCase(private val todoListRepository: TodoListRepository) {

    fun getTodoItem(id: String): TodoItem = todoListRepository.getTodoItem(id)
}