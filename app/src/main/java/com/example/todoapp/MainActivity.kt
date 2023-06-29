package com.example.todoapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.example.todoapp.localbase.ViewModelFactory

class MainActivity : AppCompatActivity()/*,GenVMInterface*/ {





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*val db = MainDb.getDb(this)
        Thread{
            db.getDao().insertItem(item)
        }.start()*/


     /*   val retrofit = Retrofit.Builder().baseUrl("https://beta.mrdekk.ru/todobackend")
            .addConverterFactory(GsonConverterFactory.create()).build()
        val todoListRepository = retrofit.create(TodoListRepository::class.java)*/




    }

    /*override fun getMainViewModel(): MainViewModel {
        return viewModel
    }*/
}