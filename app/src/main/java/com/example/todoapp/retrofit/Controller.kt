package com.example.todoapp.retrofit

import com.example.todoapp.TodoItem
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.function.Consumer


class Controller : Callback<List<TodoItem?>> {
/*    fun start() {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val todoApi: todoApi = retrofit.create(todoApi::class.java)

        *//*val call: Call<List<TodoItem>> = todoApi.updateTodoList("status:open")

        call.enqueue(this)*//*
    }*/

    override fun onResponse(call: Call<List<TodoItem?>>, response: Response<List<TodoItem?>>) {
        if (response.isSuccessful) {
            val changesList: List<TodoItem?> = response.body()!!
            /*changesList.forEach(Consumer<TodoItem> { change: TodoItem ->
                *//*System.out.println(
                    change.subject
                )*//*
            })*/
        } else {
            println(response.errorBody())
        }
    }

    override fun onFailure(call: Call<List<TodoItem?>>, t: Throwable) {
        t.printStackTrace()
    }

    /*companion object {
        const val BASE_URL = "https://git.eclipse.org/r/"
    }*/
}