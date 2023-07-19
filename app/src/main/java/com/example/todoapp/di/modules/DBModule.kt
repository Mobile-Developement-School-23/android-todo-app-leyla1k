package com.example.todoapp.di.modules


import android.content.Context
import androidx.room.Room
import com.example.todoapp.di.AppContext
import com.example.todoapp.storage.localbase.MainDb
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DBModule {
    @Singleton
    @Provides
    fun provideMyAppDatabase(@AppContext context : Context) : MainDb =
        Room.databaseBuilder(context, MainDb::class.java, "main.db").build()

    @Singleton
    @Provides
    fun provideToDoListDao(db: MainDb) = db.getDao()
}