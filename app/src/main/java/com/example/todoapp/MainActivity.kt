package com.example.todoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.todoapp.R
import com.example.todoapp.retrofit.TodoListRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



     /*   val retrofit = Retrofit.Builder().baseUrl("https://beta.mrdekk.ru/todobackend")
            .addConverterFactory(GsonConverterFactory.create()).build()
        val todoListRepository = retrofit.create(TodoListRepository::class.java)*/




    }
}