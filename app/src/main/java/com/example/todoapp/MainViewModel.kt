package com.example.todoapp

import androidx.lifecycle.ViewModel
import com.example.todoapp.retrofit.TodoItem
import com.example.todoapp.retrofit.TodoListRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainViewModel : ViewModel() {


    val retrofit = Retrofit.Builder().baseUrl("https://beta.mrdekk.ru/todobackend")
        .addConverterFactory(GsonConverterFactory.create()).build()
    val todoListRepository = retrofit.create(TodoListRepository::class.java)


   // private val repository = TodoListRepositoryImpl

    private val repository = todoListRepository.getTodoList()


    private val getTodoListUseCase = GetTodoListUseCase(repository)
    private val getTodoItemUseCase = GetTodoItemUseCase(repository)
    private val editTodoItemUseCase = EditTodoItemUseCase(repository)
    private val addTodoItemUseCase = AddTodoItemUseCase(repository)
    private val deleteTodoItemUseCase = DeleteTodoItemUseCase(repository)

    val todoList = getTodoListUseCase.getTodoList()


    fun getTodoItem(id: String) : TodoItem {
        return getTodoItemUseCase.getTodoItem(id)
    }

    fun editTodoItem(item: TodoItem) {
        editTodoItemUseCase.editTodoItem(item)
    }

    fun addTodoItem(item: TodoItem) {
        addTodoItemUseCase.addTodoItem(item)
    }

    fun changeDoneState(item: TodoItem){
        val newItem = item.copy(isCompleted = ! item.isCompleted)
        editTodoItemUseCase.editTodoItem(newItem)
    }

    fun deleteTodoItem(todoItem: TodoItem){
        deleteTodoItemUseCase.deleteTodoItem(todoItem)
    }


}