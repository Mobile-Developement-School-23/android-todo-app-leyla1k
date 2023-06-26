package com.example.todoapp

import com.example.todoapp.retrofit.TodoItem
import com.example.todoapp.retrofit.TodoListRepository

class EditTodoItemUseCase(private val todoListRepository: TodoListRepository) {

    fun editTodoItem(item: TodoItem) = todoListRepository.editTodoItem(item)
}