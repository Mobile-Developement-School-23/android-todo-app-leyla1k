package com.example.todoapp

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.todoapp.localbase.MainDb
import com.example.todoapp.retrofit.TodoApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TodoApplication: Application() {
    private val database by lazy { MainDb.getDb(this) }
    val todoListRepositoryImpl by lazy{TodoListRepositoryImpl(database.getDao(), todoApi)}


    private val retrofit by lazy {Retrofit.Builder().client(client).baseUrl("https://beta.mrdekk.ru/todobackend/")
        .addConverterFactory(GsonConverterFactory.create()).build()}
    val todoApi by lazy { retrofit.create(TodoApi::class.java)}

    val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(Interceptor { chain ->
        val newRequest: Request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer subescheator")
            .build()
        chain.proceed(newRequest)
    }).build()

    companion object {
        const val CHANNEL_ID = "TodoList"
        const val CHANNEL_NAME = "TodoListChannel"

        @Volatile
        private var instance: TodoApplication? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: TodoApplication().also { instance = it }
            }
    }
    /////
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.getDefaultNightMode())

    }


}