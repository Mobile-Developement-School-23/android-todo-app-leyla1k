package com.example.todoapp

import android.util.Log
import com.example.todoapp.localbase.TodoItemDao
import com.example.todoapp.localbase.toDbModel
import com.example.todoapp.localbase.toListOfToDoEntyty
import com.example.todoapp.retrofit.todoApi
import kotlinx.coroutines.flow.*

class TodoListRepositoryImpl(private val dao: TodoItemDao/*, private val todoApi: todoApi*/) : TodoListRepository {

    init{
        var count: Int = 1
        count=count+1
        Log.d("countReposit", "((")

    }



     override fun getTodoList(): Flow<List<TodoItem>> {
         val toDoItemList = dao.getTodoListFlow()

         val convertedToDoItemList = toDoItemList.map {
        println("DEBAG2 +${it.size}")
             it.toListOfToDoEntyty() }

        return convertedToDoItemList
    }


    override suspend fun editTodoItem(item: TodoItem) {
        dao.updateNote(item.toDbModel())
    }

    override suspend fun addTodoItem(item: TodoItem) {
        Log.d("TodoListRepositoryImpl", "addTodoItem: insertion")
        dao.insertTodoItem( item.toDbModel())
    }

    override suspend fun deleteTodoItem(item: TodoItem, stringId:String) {
        dao.deleteTodoItem(stringId)
//затем проверяем есть ли сеть и если что работать с ворк менеджерром
    }

    override suspend fun deleteTodoItemWithoutPosition(item: TodoItem) {
        dao.deleteTodoItem(item.id)
    }


    override suspend fun updateTodoList() {
        //todoApi.updateTodoList()
    }





}