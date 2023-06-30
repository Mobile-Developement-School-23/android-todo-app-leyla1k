package com.example.todoapp

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(private val todoListRepository: TodoListRepository) : ViewModel() {

    private val _listOfNotesFlow = MutableStateFlow<List<TodoItem>>(emptyList())
    val listOfNotesFlow: StateFlow<List<TodoItem>> = _listOfNotesFlow.asStateFlow()


    private val _countOfDoneFlow = MutableStateFlow<Int>(0)
    val countOfDoneFlow: StateFlow<Int> = _countOfDoneFlow.asStateFlow()

    private lateinit var getListJob: Job


    init {
        getListOfNotes()
        recalculationOfDoneTodos()//сликом быстро идет
        Log.d("DEBAG1", "Creation vm")

       /* viewModelScope.launch {
            todoListRepository.updateTodoList()
        }*/
        getListJob.cancel()

    }


    fun recalculationOfDoneTodos(){
       var count=0
        viewModelScope.launch {
            delay(500)//потом этот момент доработаю
            listOfNotesFlow.value.forEach { element ->
                if (element.isCompleted){
                   count+=1
                }

            }
            _countOfDoneFlow.value = count
            Log.d("MainViewModel", "count=" + _countOfDoneFlow.value)
            getListOfNotes()
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
            }
        }
    }

    suspend fun <T> Flow<List<T>>.flattenToList() =
        flatMapConcat { it.asFlow() }.toList()

    suspend fun getToDoListFromFlow(todoListFlow: Flow<List<TodoItem>>): List<TodoItem> {//переименовать!
        return todoListFlow.flattenToList()
    }


    fun getTodoItem(id: String): TodoItem {
        // (перебор пока не найдешь с айди)
        return listOfNotesFlow.value.find { it.id == id } ?: throw RuntimeException("not found")
        //return todoListRepository.getTodoItem(id)
    }

     fun editTodoItem(item: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            todoListRepository.editTodoItem(item)

        }
        //getListOfNotes()
    }

    /*fun addTodoItem(item: TodoItem) {
        addTodoItemUseCase.addTodoItem(item)
    }*/


    fun addTodoItem(item: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            getListOfNotes()
            // println("DEBAG2 + added into rep")
            todoListRepository.addTodoItem(item)
        }
    }


    fun changeDoneState(item: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            val newItem = item.copy(isCompleted = !item.isCompleted)

            todoListRepository.editTodoItem(newItem)

            recalculationOfDoneTodos()
            getListOfNotes()

        }
    }


    fun deleteTodoItem(item: TodoItem, position: Int) {
        viewModelScope.launch(Dispatchers.IO) {

            todoListRepository.deleteTodoItem(item, listOfNotesFlow.value.get(position).id)
            getListOfNotes()
            recalculationOfDoneTodos()
        }

    }

    fun deleteTodoItemWithoutPosition(item: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {

            todoListRepository.deleteTodoItemWithoutPosition(item)
            getListOfNotes()
        }

    }


}