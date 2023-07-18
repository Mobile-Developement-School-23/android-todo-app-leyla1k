package com.example.todoapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.TodoItem
import com.example.todoapp.di.TodoScope
import com.example.todoapp.storage.repository.TodoListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddTodoViewModel @Inject constructor(
    private val todoListRepository: TodoListRepository
): ViewModel() {

    private var _eventNetworkError = MutableLiveData(false)
    private var _isNetworkErrorShown = MutableLiveData(false)

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

    private fun onSuccessResponse() {///////тута
        _eventNetworkError.postValue(false)
        _isNetworkErrorShown.postValue(false)
    }

    private fun onUnsuccessfulResponse() {
        _eventNetworkError.postValue(true)
    }
}