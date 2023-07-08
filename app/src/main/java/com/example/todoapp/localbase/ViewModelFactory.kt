package com.example.todoapp.localbase


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.MainViewModel
import com.example.todoapp.TodoListRepository
import com.example.todoapp.TodoListRepositoryImpl
import javax.inject.Inject
import javax.inject.Provider


class ViewModelFactory @Inject constructor(private val viewModelProviders:@JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return viewModelProviders[modelClass]?.get() as T
    }
}


/*
class ViewModelFactory(
    val todoListRepository: TodoListRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(todoListRepository) as T
        }

        throw IllegalArgumentException("UNKNOWN VIEW MODEL CLASS")
    }
}*/
