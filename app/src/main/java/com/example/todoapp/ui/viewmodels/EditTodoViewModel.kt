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

class EditTodoViewModel @Inject constructor(
    private val todoListRepository: TodoListRepository
): ViewModel() {
    private var _eventNetworkError = MutableLiveData(false)
    private var _isNetworkErrorShown = MutableLiveData(false)

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

    suspend fun getTodoItem(id: String): TodoItem {
        return todoListRepository.getItemByID(id)
    }

    private fun onSuccessResponse() {
        _eventNetworkError.postValue(false)
        _isNetworkErrorShown.postValue(false)
    }

    private fun onUnsuccessfulResponse() {
        _eventNetworkError.postValue(true)
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