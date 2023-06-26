package com.example.todoapp

import com.example.todoapp.retrofit.TodoItem
import com.example.todoapp.retrofit.TodoListRepository

class AddTodoItemUseCase(private val todoListRepository: TodoListRepository) {

    fun addTodoItem(item: TodoItem) = todoListRepository.addTodoItem(item)

}