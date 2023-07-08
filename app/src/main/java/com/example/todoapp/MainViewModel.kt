package com.example.todoapp

import android.util.Log
import androidx.lifecycle.*
import com.example.todoapp.network.TodoListRequestDto
import com.example.todoapp.network.mapListDtoToListEntity
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

        viewModelScope.launch {
            //сначала сделать пааачччч
           // getListOfNotesFromInternet(downloadTodoList())


            listFromServerToDb()
           /* Log.d(
                "MainViewModel",
                "downloadTodoList: " + downloadTodoList()!!.size.toString()
            )
            Log.d("MainViewModel", "downloadTodoList: " + downloadTodoList())*/

        }

        getListJob.cancel()
    }

    suspend fun listFromServerToDb(){
       // getListOfNotesFromInternet(downloadTodoList()) //вероятно не нужный метод
      //// // updateTodoListFromInternet()/////надо это па

        deleteList()

        downloadTodoList()!!.forEach {
           addTodoItem(/*revision*/true,1,it)//я плохо понимаю как работать с ревизиями,
        // поэтому пока не разберусь только так(
        }
        getListOfNotes()
    }
    suspend fun updateTodoListFromInternet(revision: Int, body: TodoListRequestDto){
        todoListRepository.updateTodoListFromInternet(revision, body)
    }
suspend fun deleteList(){
    todoListRepository.deleteList()
}



    suspend fun downloadTodoList():List<TodoItem>?{
        return todoListRepository.downloadTodoList()

}


    fun recalculationOfDoneTodos() {
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
            getListOfNotes()
        }

    }
    private fun getListOfNotesFromInternet(list : List<TodoItem>?) {
        getListJob = viewModelScope.launch {

                _listOfNotesFlow.update {
                    list!!.toMutableList().apply {
                        addAll(list!!.map { noteData ->
                            noteData.copy()
                        })

                    }

                }
            //Log.d("MainViewModel", "getListOfNotesFromInternet: "+ _listOfNotesFlow)
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
            //или тут или сначала из нета подгрузим, предварительно проверив что он у нас есть
            /*Log.d("MainViewModel", "mergeFromList: "+todoListRepository.updateListFromInternet())
            todoListRepository.updateListFromInternet()*/
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



    fun addTodoItem(isConnected:Boolean, revision: Int, item: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {
            getListOfNotes()
            // println("DEBAG2 + added into rep")
            todoListRepository.addTodoItem(item)
if(isConnected) {
    todoListRepository.addTodoItemToInternet(revision, item)
}
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


    fun deleteTodoItem(isConnected: Boolean,item: TodoItem, position: Int) {
        viewModelScope.launch(Dispatchers.IO) {

            todoListRepository.deleteTodoItem(item, listOfNotesFlow.value.get(position).id)
            getListOfNotes()
            recalculationOfDoneTodos()

            if(isConnected){
                todoListRepository.deleteTodoItemFromInternet(1,listOfNotesFlow.value.get(position).id)
            }
        }

    }

    fun deleteTodoItemWithoutPosition(item: TodoItem) {
        viewModelScope.launch(Dispatchers.IO) {

            todoListRepository.deleteTodoItemWithoutPosition(item)
            getListOfNotes()
        }

    }


}