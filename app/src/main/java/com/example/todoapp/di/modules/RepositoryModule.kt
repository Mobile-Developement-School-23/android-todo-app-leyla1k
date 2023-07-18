package com.example.todoapp.di.modules

import com.example.todoapp.TodoListRepositoryImpl
import com.example.todoapp.storage.repository.TodoListRepository
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {
    @Binds
    abstract fun provideTodoItemRepository(todoListRepositoryImpl: TodoListRepositoryImpl): TodoListRepository
}