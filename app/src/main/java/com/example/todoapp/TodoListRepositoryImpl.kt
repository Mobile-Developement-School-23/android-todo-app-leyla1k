package com.example.todoapp

import android.util.Log
import com.example.todoapp.localbase.TodoItemDao
import com.example.todoapp.localbase.toDbModel
import com.example.todoapp.localbase.toListOfToDoEntyty
import com.example.todoapp.network.TodoListRequestDto
import com.example.todoapp.network.mapEntityToDto
import com.example.todoapp.network.mapEntityToItemRequestDto
import com.example.todoapp.network.mapListDtoToListEntity
import com.example.todoapp.retrofit.todoApi
import kotlinx.coroutines.flow.*
import retrofit2.http.Body
import java.util.*

class TodoListRepositoryImpl(private val dao: TodoItemDao, private val todoApi: todoApi) :
    TodoListRepository {

    init {
        var count: Int = 1
        count = count + 1
        Log.d("countReposit", "((")

    }

    override suspend fun addTodoItemToInternet(revision: Int, item: TodoItem) {
        Log.d("TodoListRepositoryImpl", "addTodoItemToIInternet: insertion")
        Log.d("TodoListRepositoryImpl", "viewOfDtoItem: " + mapEntityToDto(item))
        Log.d("TodoListRepositoryImpl", "viewOfItemRequestDto: " + mapEntityToItemRequestDto(item))
        todoApi.loadTodoItem(
            revision,
            mapEntityToItemRequestDto(item)
        )//requireContext().getRevision()
        Log.d("TodoListRepositoryImpl", "downloadTodoList: " + downloadTodoList())


    }

    override suspend fun deleteList() {
        dao.deleteList()
    }
    /*override suspend fun updateTodoListFromInternet*/

    override suspend fun updateListFromInternet(
        revision: Int,
        body: TodoListRequestDto
    ) {//имеет смысл сделать интерфейс и оверрайд?
        todoApi.updateTodoListFromInternet(revision, body)
    }

    override suspend fun downloadTodoList(): List<TodoItem>? {
        /*val response = todoApi.downloadTodoList().body()
        context.setRevision(response!!.revision)*/

        return mapListDtoToListEntity(todoApi.downloadTodoList().body()!!.list)
    }

    override suspend fun deleteTodoItemFromInternet(revision: Int,id:String) {

        todoApi.deleteTodoItem(1,UUID.fromString(id))
        Log.d("TodoListRepositoryImpl", "deleteTodoItemFromInternet: "+ id)
    }


    /////////////////////////////////////
    override fun getTodoList(): Flow<List<TodoItem>> {

        val toDoItemList = dao.getTodoListFlow()
        val convertedToDoItemList = toDoItemList.map {
            println("DEBAG2 +${it.size}")
            it.toListOfToDoEntyty()
        }

        return convertedToDoItemList
    }


    override suspend fun editTodoItem(item: TodoItem) {
        dao.updateNote(item.toDbModel())
    }

    override suspend fun addTodoItem(item: TodoItem) {
        Log.d("TodoListRepositoryImpl", "addTodoItem: insertion")
        dao.insertTodoItem(item.toDbModel())
    }

    override suspend fun updateTodoListFromInternet( revision: Int, body: TodoListRequestDto) {
        todoApi.updateTodoListFromInternet( revision, body)/////////
    }

    override suspend fun deleteTodoItem(item: TodoItem, stringId: String) {
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