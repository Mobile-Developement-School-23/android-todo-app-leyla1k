package com.example.todoapp.retrofit

import com.example.todoapp.network.AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path


interface todoApi {


    fun myHttpClient(): OkHttpClient {
        val builder = OkHttpClient().newBuilder()
            .addInterceptor(AuthInterceptor())
        return builder.build()
    }

    @GET("https://beta.mrdekk.ru/todobackend/list")
    suspend fun getTodoList()


    @GET("https://beta.mrdekk.ru/todobackend/list/{id}")//<>?????
    suspend fun getTodoById(@Path("id") id:String): TodoItemModel


    @PATCH("https://beta.mrdekk.ru/todobackend/list")
   // Authorization: Bearer abc123"subescheator": "Karimova_L"
    suspend fun updateTodoList()


}