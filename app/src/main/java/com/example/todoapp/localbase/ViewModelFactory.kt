package com.example.todoapp.localbase


import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.MainViewModel
import com.example.todoapp.TodoListRepository
import com.example.todoapp.TodoListRepositoryImpl
import javax.inject.Inject
import javax.inject.Provider



class ViewModelFactory(
    val todoListRepository: TodoListRepository,
    val app:Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(todoListRepository, app) as T
            }

        throw IllegalArgumentException("UNKNOWN VIEW MODEL CLASS")
    }
}
