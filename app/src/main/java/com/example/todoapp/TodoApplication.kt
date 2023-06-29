package com.example.todoapp

import android.app.Application
import androidx.fragment.app.viewModels
import com.example.todoapp.localbase.MainDb
import com.example.todoapp.localbase.ViewModelFactory

class TodoApplication: Application() {
    private val database by lazy { MainDb.getDb(this) }
    val todoListRepositoryImpl by lazy{TodoListRepositoryImpl(database.getDao())}




}