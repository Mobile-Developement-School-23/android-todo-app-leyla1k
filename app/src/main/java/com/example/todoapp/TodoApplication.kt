package com.example.todoapp

import android.app.Application
import androidx.fragment.app.viewModels
import com.example.todoapp.localbase.MainDb
import com.example.todoapp.localbase.ViewModelFactory
import com.example.todoapp.retrofit.todoApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TodoApplication: Application() {
    private val database by lazy { MainDb.getDb(this) }
    val todoListRepositoryImpl by lazy{TodoListRepositoryImpl(database.getDao(), todoApi)}


/*val authToken = "your_auth_token"
val client = OkHttpClient.Builder()
    .addInterceptor(AuthInterceptor(authToken))
    .build()*/

    private val retrofit by lazy {Retrofit.Builder().client(client).baseUrl("https://beta.mrdekk.ru/todobackend/")
        .addConverterFactory(GsonConverterFactory.create()).build()}
    val todoApi by lazy { retrofit.create(todoApi::class.java)}

    val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(Interceptor { chain ->
        val newRequest: Request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer subescheator")
            .build()
        chain.proceed(newRequest)
    }).build()



}