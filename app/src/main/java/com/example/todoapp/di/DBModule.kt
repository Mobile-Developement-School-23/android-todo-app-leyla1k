package com.example.todoapp.di

import android.content.Context
import androidx.room.Room
import com.example.todoapp.localbase.MainDb
import dagger.Module
import dagger.Provides

@Module
class DBModule {

    @ApplicationScope
    @Provides
    fun provideMyAppDatabase(context : Context) : MainDb {
        return Room.databaseBuilder(context, MainDb::class.java, "main.db").build()
    }

    @ApplicationScope
    @Provides
    fun provideToDoListDao(db:MainDb)=db.getDao()

}