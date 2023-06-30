package com.example.todoapp

import android.app.Application
import androidx.fragment.app.viewModels
import com.example.todoapp.localbase.MainDb
import com.example.todoapp.localbase.ViewModelFactory
import com.example.todoapp.retrofit.todoApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TodoApplication: Application() {
    private val database by lazy { MainDb.getDb(this) }
    val todoListRepositoryImpl by lazy{TodoListRepositoryImpl(database.getDao()/*, todoApi*/)}


/*val authToken = "your_auth_token"
val client = OkHttpClient.Builder()
    .addInterceptor(AuthInterceptor(authToken))
    .build()

    private val retrofit by lazy {Retrofit.Builder().baseUrl("https://beta.mrdekk.ru/todobackend/")
        .addConverterFactory(GsonConverterFactory.create()).build()}
    val todoApi by lazy { retrofit.create(todoApi::class.java)}*/





}