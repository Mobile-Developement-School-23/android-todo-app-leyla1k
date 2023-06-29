package com.example.todoapp.domain

import androidx.lifecycle.LiveData
import com.example.todoapp.TodoListRepository
import com.example.todoapp.retrofit.TodoItem

class GetTodoListUseCase(private val todoListRepository: TodoListRepository) {

    //fun getTodoList(): LiveData<List<TodoItem>> = todoListRepository.getTodoList()
}


