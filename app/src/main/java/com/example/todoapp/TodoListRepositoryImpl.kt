package com.example.todoapp

import android.util.Log
import com.example.todoapp.localbase.TodoItemDao
import com.example.todoapp.localbase.toDbModel
import com.example.todoapp.localbase.toListOfToDoEntyty
import com.example.todoapp.retrofit.ItemPriority
import com.example.todoapp.retrofit.TodoItem
import kotlinx.coroutines.flow.*
import java.util.*

class TodoListRepositoryImpl(private val dao: TodoItemDao) : TodoListRepository {

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

   /* override fun getTodoItem(id: String): TodoItem {
        return todoList.find { it.id == id } ?: throw RuntimeException("not found")
       // return TodoItem("","",ItemPriority.LOW,null,true, Calendar.getInstance().time,null)
    }*/

    override suspend fun editTodoItem(item: TodoItem) {

        dao.updateNote(item.toDbModel())


    }

    override suspend fun addTodoItem(item: TodoItem) {
        Log.d("TodoListRepositoryImpl", "addTodoItem: insertion")
        dao.insertTodoItem( item.toDbModel())
    }

    override suspend fun deleteTodoItem(item: TodoItem,stringId:String) {
        dao.deleteTodoItem(stringId)

    }

    override suspend fun deleteTodoItemWithoutPosition(item: TodoItem) {
        dao.deleteTodoItem(item.id)
    }







}