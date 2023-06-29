package com.example.todoapp

import android.util.Log
import androidx.lifecycle.*
import com.example.todoapp.domain.*
import com.example.todoapp.retrofit.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel(private val todoListRepository: TodoListRepository) : ViewModel() {

    private val _listOfNotesFlow = MutableStateFlow<List<TodoItem>>(emptyList())
    val listOfNotesFlow: StateFlow<List<TodoItem>> = _listOfNotesFlow.asStateFlow()
    private lateinit var getListJob: Job

    //var todoListLiveData = MutableLiveData<List<TodoItem>>()
    /* val todoListLiveData : MutableLiveData<List<TodoItem>> by lazy{
        MutableLiveData<List<TodoItem>>()
    }*/


    /*private*/ //var todoList: List<TodoItem> =  mutableListOf<TodoItem>()

/*    val someData: LiveData<Boolean>
        get() = todoListLiveData*/


    //val todoList = getTodoListUseCase.getTodoList()

    init {
        getListOfNotes()
        Log.d("DEBAG1","Creation vm")

        viewModelScope.launch(Dispatchers.IO)
        {

                getListOfNotes()

                listOfNotesFlow.collect {
                    Log.d("DEBAG4", "listOfNotesFlow" + it.size.toString())
                }
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
              _listOfNotesFlow.collect {
                    Log.d("DEBAG111", it.size.toString())
                }
            }
        }
    }

    suspend fun <T> Flow<List<T>>.flattenToList() =
        flatMapConcat { it.asFlow() }.toList()

    suspend fun getToDoListFromFlow(todoListFlow: Flow<List<TodoItem>>): List<TodoItem> {//переименовать!
        return todoListFlow.flattenToList()
    }


    /*suspend fun updateTodoList() {///
        todoListLiveData.postValue(todoList )

        Log.d("updateTodoList", "inFun")
        Log.d("updateTodoList", todoListLiveData.value.toString()+ " =livedata")

    }*/

    /*fun getTodoItem(id: String): TodoItem {
        return getTodoItemUseCase.getTodoItem(id)
    }*/

    /* fun editTodoItem(item: TodoItem) {
         editTodoItemUseCase.editTodoItem(item)
     }*/

    /*fun addTodoItem(item: TodoItem) {
        addTodoItemUseCase.addTodoItem(item)
    }*/


     fun addTodoItem(item: TodoItem) {

        viewModelScope.launch(Dispatchers.IO) {
            getListOfNotes()
            println("DEBAG2 + added into rep")
            todoListRepository.addTodoItem(item)
        }
    }


    /*   fun changeDoneState(item: TodoItem) {
           val newItem = item.copy(isCompleted = !item.isCompleted)
           editTodoItemUseCase.editTodoItem(newItem)
       }

       fun deleteTodoItem(todoItem: TodoItem) {
           deleteTodoItemUseCase.deleteTodoItem(todoItem)
       }
   */

}