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
        viewModelScope.launch {
            getListOfNotes()
            refreshDataFromRepository()
            todoListRepository.createRevision()
        }

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
       private fun recalculationOfDoneTodos() {
        var count = 0
        viewModelScope.launch {
            listOfNotesFlow.value.forEach { element ->
                if (element.isCompleted) {
                    count += 1
                }
            }
            _countOfDoneFlow.value = count
            Log.d("MainViewModel", "count=" + _countOfDoneFlow.value)

        }
    }
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

                recalculationOfDoneTodos()
            }

        }
    }
    suspend fun <T> Flow<List<T>>.flattenToList() =
        flatMapConcat { it.asFlow() }.toList()
    fun getTodoItem(id: String): TodoItem {
        return listOfNotesFlow.value.find { it.id == id } ?: throw RuntimeException("not found")
    }
    fun editTodoItem(item: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            todoListRepository.editTodoItem(item)
            try {
                todoListRepository.editTodoItemToInternet(item)
                onSuccessResponse()
            } catch (e: Exception) {
                onUnsuccessfulResponse()
                Log.v("updateItem", e.message.toString())
            }
        }
    }
    fun addTodoItem(item: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
           // getListOfNotes()
            Log.d("insertItem", "insert")
            todoListRepository.addTodoItem(item)
            try {
                todoListRepository.addTodoItemToInternet(item)
                onSuccessResponse()
            } catch (e: Exception) {
                onUnsuccessfulResponse()
                Log.v("insertItem", e.message.toString())
            }
        }
    }
    fun changeDoneState(item: TodoItem) {// поменять!!! эдит можно тот что выше один раз сделать!
        viewModelScope.launch(Dispatchers.IO) {
            val newItem = item.copy(isCompleted = !item.isCompleted)
            todoListRepository.editTodoItem(newItem)
            try {
                todoListRepository.editTodoItemToInternet(newItem)
                onSuccessResponse()
            } catch (e: Exception) {
                onUnsuccessfulResponse()
                Log.v("editItem", e.message.toString())
            }
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

        }
    }
    fun deleteTodoItemWithoutPosition(item: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            todoListRepository.deleteTodoItemWithoutPosition(item)
            try {
                todoListRepository.deleteTodoItemFromInternet(item.id)
                onSuccessResponse()
            } catch (e: Exception) {
                onUnsuccessfulResponse()
                Log.v("deleteItem", e.message.toString())
            }
        }
    }
}