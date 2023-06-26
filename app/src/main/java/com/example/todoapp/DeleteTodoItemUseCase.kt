package com.example.todoapp

import com.example.todoapp.retrofit.TodoItem
import com.example.todoapp.retrofit.TodoListRepository

class DeleteTodoItemUseCase(private val todoListRepository: TodoListRepository) {

    fun deleteTodoItem(item: TodoItem){
        todoListRepository.deleteTodoItem(item)
    }
}