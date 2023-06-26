package com.example.todoapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.todoapp.retrofit.TodoItem
import com.example.todoapp.retrofit.TodoListRepository
import java.util.Calendar

object TodoListRepositoryImpl : TodoListRepository {

    private val todoList = mutableListOf<TodoItem>()
    private val todoListLiveData = MutableLiveData<List<TodoItem>>()

    init {
        todoList.add(
            TodoItem(
                "0",
                "Доделать домашку по алгосам",
                TodoItem.ItemPriority.LOW,
                isCompleted = false,
                createDate = Calendar.getInstance().time,
                deadline = null,
                changedDate = null
            )
        )

        todoList.add(
            TodoItem(
                "10",
                "Устроить гос переворот в Восточном Тиморе",
                TodoItem.ItemPriority.URGENT,
                isCompleted = false,
                createDate = Calendar.getInstance().time,
                deadline = null,
                changedDate = null
            )
        )

        updateTodoList()
    }

    override fun getTodoList(): LiveData<List<TodoItem>> {
        return todoListLiveData
    }

    override fun getTodoItem(id: String): TodoItem {
        return todoList.find { it.id == id } ?: throw RuntimeException("not found")
    }

    override fun editTodoItem(item: TodoItem) {
        val oldElement = todoList.find { it.id == item.id }
        val index = todoList.indexOf(oldElement)

        todoList[index] = item

        updateTodoList()
    }

    override fun addTodoItem(item: TodoItem) {
        todoList.add(item)

        updateTodoList()
    }

    override fun deleteTodoItem(item: TodoItem) {
        todoList.remove(item)

        updateTodoList()
    }


    private fun updateTodoList() {
        todoListLiveData.value = todoList.toList()
    }
}