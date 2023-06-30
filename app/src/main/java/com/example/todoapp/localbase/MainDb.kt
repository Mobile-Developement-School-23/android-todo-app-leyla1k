package com.example.todoapp.localbase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database (entities = [TodoDataItem::class], version = 1)
@TypeConverters(Converters::class)
abstract class MainDb : RoomDatabase() {
    abstract fun getDao(): TodoItemDao

    companion object {

        @Volatile
        private var INSTANCE: MainDb? = null


        fun getDb(context: Context): MainDb {
            return INSTANCE ?: synchronized(this)
            {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MainDb::class.java,
                    "test.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }

    }
}