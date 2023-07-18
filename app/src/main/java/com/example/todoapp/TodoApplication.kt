package com.example.todoapp

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.todoapp.di.ApplicationComponent
import com.example.todoapp.di.DaggerApplicationComponent
import com.example.todoapp.storage.localbase.MainDb
import com.example.todoapp.network.TodoApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TodoApplication: Application() {
    private val retrofit by lazy {Retrofit.Builder().client(client).baseUrl("https://beta.mrdekk.ru/todobackend/")
        .addConverterFactory(GsonConverterFactory.create()).build()}

    val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(Interceptor { chain ->
        val newRequest: Request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer subescheator")
            .build()
        chain.proceed(newRequest)
    }).build()

    val applicationComponent: ApplicationComponent by lazy {
        DaggerApplicationComponent.factory().create(//а тут супа пупа
            application = this,
            context = applicationContext,
            retrofit = retrofit)
    }

    companion object {

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