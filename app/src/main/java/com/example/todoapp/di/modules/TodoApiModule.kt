package com.example.todoapp.di.modules

import com.example.todoapp.network.TodoApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
object TodoApiModule {
    @Provides
    fun provideTodoApi(retrofit: Retrofit) = retrofit.create(TodoApi::class.java)
}