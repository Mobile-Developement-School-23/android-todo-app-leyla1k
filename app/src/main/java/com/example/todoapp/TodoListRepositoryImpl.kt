package com.example.todoapp

import android.util.Log
import com.example.todoapp.localbase.DbRevision
import com.example.todoapp.localbase.TodoDataItem
import com.example.todoapp.localbase.TodoItemDao
import com.example.todoapp.localbase.toDbModel

import com.example.todoapp.localbase.toListOfToDoEntyty
import com.example.todoapp.localbase.toTodoItem
import com.example.todoapp.network.TodoListRequestDto
import com.example.todoapp.network.TodoListResponseDto
import com.example.todoapp.network.mapDtoToTodoItem
import com.example.todoapp.network.mapTodoItemToItemRequestDto
import com.example.todoapp.network.mapListDtoToTodoItemList
import com.example.todoapp.network.mapTodoItemToDto
import com.example.todoapp.retrofit.TodoApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import java.util.*

class TodoListRepositoryImpl(private val dao: TodoItemDao, private val todoApi: TodoApi) :
    TodoListRepository {
    override suspend fun refreshData() {
        withContext(Dispatchers.IO) {

            val request = todoApi.getServerResponse()

            val localRevision = getRevision()
            Log.d("localRevision", "refreshData: " + localRevision.toString())
            if (localRevision > request.revision) {
                updateServerFromDatabase(request)
            } else {

                updateDatabaseFromServer(request)
            }
            synchronizeRevisions()
        }
    }
    private suspend fun updateServerFromDatabase(request: TodoListResponseDto) {

        dao.getTodoListFlow().collect { itm ->
            if (!itm.isNullOrEmpty()) {
                val currentRequest =
                    TodoListRequestDto(/*status,*/  itm.map {
                        mapTodoItemToDto(it.toTodoItem())
                    })
                todoApi.updateServerFromDb(request.revision, currentRequest)
            }
        }
    }
    override suspend fun createRevision() {
        try {
            dao.getRevisionForCreating(1).value
            Log.d("DEBAGGING2", "createRevision: ")
        } catch (e: Exception) {
            Log.d("DEBAGGING", "createRevision: ")
            dao.insertRevision(DbRevision(1, 0))
        }
    }
    private suspend fun updateDatabaseFromServer(request: TodoListResponseDto) {
        val itemsFromServer = request.list.map {
            mapDtoToTodoItem(it).toDbModel()
        }
        val itemsFromDatabase = dao.getTodoListAsList()

        if (itemsFromDatabase.isNullOrEmpty()) {
            itemsFromDatabase.map {
                dao.insertTodoItem(it)
            }
        } else {
            Log.d("DebagerBig", "updateDatabaseFromServer: ")
            mergeData(itemsFromDatabase, itemsFromServer)
        }


    }



    private suspend fun synchronizeRevisions() {
        withContext(Dispatchers.IO) {
            val response = todoApi.getServerResponse()
            dao.updateRevision(DbRevision(Constants.REVISION_ID, response.revision))
        }
    }
    private suspend fun mergeData(///или мердж бесконечный или список
        itemsFromDatabase: List<TodoDataItem>,
        itemsFromServer: List<TodoDataItem>
    ) {
        for (item in itemsFromDatabase) {
            if (itemsFromServer.find { it.id == item.id } == null) {
                deleteItemFromDatabase(item)
            }
        }
        for (item in itemsFromServer) {
            if (itemsFromDatabase.find { it.id == item.id } != null) {

                updateItemToDatabase(item)
            } else {
                insertItemToDatabase(item)
            }
        }
    }
    private suspend fun deleteItemFromDatabase(item: TodoDataItem) {
        increaseRevision()
        dao.deleteTodoItem(item.id)
        Log.d("TAGINCR3", "!!!!!!!!!!")

    }
    private suspend fun updateItemToDatabase(item: TodoDataItem) {
        increaseRevision()
        dao.updateTodoItem(item)
        Log.d("TAGINCR4", "!!!!!!!!!!")

    }
    private suspend fun insertItemToDatabase(item: TodoDataItem) {
        increaseRevision()
        withContext(Dispatchers.IO) {
            dao.insertTodoItem(item)
        }
        Log.d("TAGINCR5", "!!!!!!!!!!")
    }
    private fun getRevision(): Int {
        val revisionObj: DbRevision = dao.getRevision()

        return revisionObj.value
    }
    private suspend fun increaseRevision() {
        val revisionObj: DbRevision = dao.getRevision()
        revisionObj.value += 1
        dao.updateRevision(revisionObj)
        Log.d("TAGINCR1", "!!!!!!!!!!")
        Log.d("TodoListRep", "updateRevision: ")
    }
    private suspend fun updateRevisionLikeServer(rev:Int) {
        val revisionObj: DbRevision = dao.getRevision()
        revisionObj.value = rev
        dao.updateRevision(revisionObj)
        Log.d("TAGINCR2", "!!!!!!!!!!")
    }
    override suspend fun editTodoItemToInternet(newItem: TodoItem) {
        val raw = todoApi.editTodoItemToInternet(
            getRevision(), UUID.fromString(newItem.id),
            mapTodoItemToItemRequestDto(newItem)
        )
        val response = raw.body()
        if (response != null) {
            updateRevisionLikeServer(response.revision)
        }
    }
    override suspend fun addTodoItemToInternet(item: TodoItem) {/////////////////////////////////////////
        Log.d("addTodoItem_responseItem", item.toString())
        val raw = todoApi.loadTodoItem(
            getRevision(),
            mapTodoItemToItemRequestDto(item)
        )
        val response = raw.body()
        if (response != null) {
            updateRevisionLikeServer(response.revision)
        }

    }
    override suspend fun deleteList() {
        dao.deleteList()
    }
    override suspend fun updateListFromInternet(
        revision: Int,
        body: TodoListRequestDto
    ) {
        todoApi.updateServerFromDb(revision, body)
    }
    override suspend fun downloadTodoList(): List<TodoItem>? {
        return mapListDtoToTodoItemList(todoApi.downloadTodoList().body()!!.list)
    }
    override suspend fun deleteTodoItemFromInternet(id: String) {
        withContext(Dispatchers.IO) {
            val raw = todoApi.deleteTodoItem(getRevision(), UUID.fromString(id))
            val response = raw.body()

            if (response != null) {
                updateRevisionLikeServer(response.revision)
            }
        }
        Log.d("TodoListRepositoryImpl", "deleteTodoItemFromInternet: " + id)
    }
    override fun getTodoList(): Flow<List<TodoItem>> {

        val toDoItemList = dao.getTodoListFlow()
        val convertedToDoItemList = toDoItemList.map {
            println("DEBAG2 +${it.size}")
            it.toListOfToDoEntyty()
        }
        return convertedToDoItemList
    }
    override suspend fun editTodoItem(item: TodoItem) {
        withContext(Dispatchers.IO) {
            dao.updateTodoItem(item.toDbModel())
        }
    }
    override suspend fun addTodoItem(item: TodoItem) {
        Log.d("TodoListRepositoryImpl", "addTodoItem: insertion")
        withContext(Dispatchers.IO) {
            dao.insertTodoItem(item.toDbModel())
        }
    }
    override suspend fun updateTodoListFromInternet(revision: Int, body: TodoListRequestDto) {
        todoApi.updateServerFromDb(revision, body)/////////
    }
    override suspend fun deleteTodoItem(item: TodoItem, stringId: String) {
        withContext(Dispatchers.IO) {
        dao.deleteTodoItem(stringId)
        }
    }
    override suspend fun deleteTodoItemWithoutPosition(item: TodoItem) {
        dao.deleteTodoItem(item.id)
    }
}