package com.example.todoapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.todoapp.network.SynchronizeWorker
import com.example.todoapp.network.TodoListRequestDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.concurrent.TimeUnit

class MainViewModel(
    private val todoListRepository: TodoListRepository,
    private val app: Application,
    private val workManager: WorkManager = WorkManager.getInstance(app)
) : ViewModel() {

    private val _listOfNotesFlow = MutableStateFlow<List<TodoItem>>(emptyList())
    val listOfNotesFlow: StateFlow<List<TodoItem>> = _listOfNotesFlow.asStateFlow()


    private val _countOfDoneFlow = MutableStateFlow<Int>(0)
    val countOfDoneFlow: StateFlow<Int> = _countOfDoneFlow.asStateFlow()

    private lateinit var getListJob: Job


    init {
        getListOfNotes()
        //recalculationOfDoneTodos()//сликом быстро идет
        viewModelScope.launch {
            // getListOfNotesFromInternet(downloadTodoList())
            getListOfNotes()
           // delay(3000)
            refreshDataFromRepository()



        }
        getListJob.cancel()
    }
    private val workRequest =
        PeriodicWorkRequestBuilder<SynchronizeWorker>(Constants.SYNCHRONIZE_INTERVAL_HOURS, TimeUnit.HOURS)
            .build()
    private var _eventNetworkError = MutableLiveData(false)
    private var _isNetworkErrorShown = MutableLiveData(false)

    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown


    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }
    /*fun insertRevision() {
        todoListRepository.insertRevision()
    }*/



    /*   private suspend fun listFromServerToDb(*//*activityCont: Context, revision: Int*//*) {
        // getListOfNotesFromInternet(downloadTodoList()) //вероятно не нужный метод
        //// // updateTodoListFromInternet()/////надо это па

        deleteList()
        delay(1000)
        downloadTodoList()!!.forEach {
            addTodoItemTemp(it)//я плохо понимаю как работать с ревизиями,
            // поэтому пока не разберусь только так(
        }
        getListOfNotes()
    }*/
     fun refreshDataFromRepository() {
        viewModelScope.launch {
            Log.d("localRevision", "refreshData: иду")
            try {
                todoListRepository.refreshData()
                  onSuccessResponse()
                Log.d("localRevision", "refreshData: иду")

            } catch (networkError: IOException) {
                  onUnsuccessfulResponse()
            }
            workManager.enqueue(workRequest)
        }


    }
    private fun onSuccessResponse() {///////тута
        _eventNetworkError.postValue(false)
        _isNetworkErrorShown.postValue(false)
    }

    private fun onUnsuccessfulResponse() {
        _eventNetworkError.postValue(true)
    }
   /* suspend fun updateTodoListFromInternet(revision: Int, body: TodoListRequestDto) {
        todoListRepository.updateTodoListFromInternet(revision, body)
    }

    private suspend fun deleteList() {
        todoListRepository.deleteList()
    }


    private suspend fun downloadTodoList(): List<TodoItem>? {
        return todoListRepository.downloadTodoList()

    }*/


    private fun recalculationOfDoneTodos() {
        var count = 0
        viewModelScope.launch {
            delay(500)//потом этот момент доработаю
            listOfNotesFlow.value.forEach { element ->
                if (element.isCompleted) {
                    count += 1
                }

            }
            _countOfDoneFlow.value = count
            Log.d("MainViewModel", "count=" + _countOfDoneFlow.value)
            //getListOfNotes()
        }

    }
  /*  private fun getListOfNotesFromInternet(list: List<TodoItem>?) {
        getListJob = viewModelScope.launch {
            _listOfNotesFlow.update {
                list!!.toMutableList().apply {
                    addAll(list!!.map { noteData ->
                        noteData.copy()
                    })
                }

            }
        }
    }*/

    private fun getListOfNotes() {
        getListJob = viewModelScope.launch {
            (todoListRepository.getTodoList()).collect { uit ->
                _listOfNotesFlow.update {
                    mutableListOf<TodoItem>().apply {
                        addAll(uit.map { noteData ->
                            noteData.copy()
                        })

                    }

                }
            }
        }
    }
    suspend fun <T> Flow<List<T>>.flattenToList() =
        flatMapConcat { it.asFlow() }.toList()
    /*suspend fun getToDoListFromFlow(todoListFlow: Flow<List<TodoItem>>): List<TodoItem> {//переименовать!
        return todoListFlow.flattenToList()
    }
    */
    fun getTodoItem(id: String): TodoItem {
        return listOfNotesFlow.value.find { it.id == id } ?: throw RuntimeException("not found")
    }
    fun editTodoItem(item: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            todoListRepository.editTodoItem(item)
            try {
                todoListRepository.editTodoItemToInternet(item)
            } catch (e: Exception) {
                Log.v("updateItem", e.message.toString())
            }
            //todoListRepository.editTodoItemToInternet(item)
        }
    }
   /* fun addTodoItemTemp(item: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            getListOfNotes()
            todoListRepository.addTodoItem(item)
        }
    }*/
    fun addTodoItem(item: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            getListOfNotes()
            todoListRepository.addTodoItem(item)
            try {
                todoListRepository.addTodoItemToInternet(item)
                onSuccessResponse()
            } catch (e: Exception) {
                onUnsuccessfulResponse()
                Log.v("insertItem", e.message.toString())
            }
            //todoListRepository.addTodoItemToInternet(item)
        }
    }
    fun changeDoneState(item: TodoItem) {// поменять!!! эдит можно тот что выше один раз сделать!
        viewModelScope.launch(Dispatchers.IO) {
            val newItem = item.copy(isCompleted = !item.isCompleted)

            todoListRepository.editTodoItem(newItem)

            recalculationOfDoneTodos()
            getListOfNotes()

            todoListRepository.editTodoItemToInternet(newItem)


        }
    }
    fun deleteTodoItem( item: TodoItem, position: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            todoListRepository.deleteTodoItem(item, listOfNotesFlow.value.get(position).id)
            try {
                todoListRepository.deleteTodoItemFromInternet(listOfNotesFlow.value.get(position).id)
                onSuccessResponse()
            } catch (e: Exception) {
                onUnsuccessfulResponse()
                Log.v("deleteItem", e.message.toString())
            }
                //todoListRepository.deleteTodoItemFromInternet(listOfNotesFlow.value.get(position).id)
        }
        recalculationOfDoneTodos()
        getListOfNotes()

    }
    fun deleteTodoItemWithoutPosition(item: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {

            todoListRepository.deleteTodoItemWithoutPosition(item)
            getListOfNotes()
        }

    }
}