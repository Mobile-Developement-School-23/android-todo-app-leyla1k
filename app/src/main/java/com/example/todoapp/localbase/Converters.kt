package com.example.todoapp.localbase

import androidx.room.TypeConverter
import com.example.todoapp.ItemPriority
import java.util.*

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

    @TypeConverter
    fun choosePriority(priority: ItemPriority): Int {
        return when (priority) {
            ItemPriority.LOW -> ItemPriority.LOW.value
            ItemPriority.NORMAL ->  ItemPriority.NORMAL.value
            ItemPriority.URGENT -> ItemPriority.URGENT.value
        }
    }
    @TypeConverter
    fun choosePriorityByInt(value: Int): ItemPriority {
        return when (value) {
            ItemPriority.LOW.value -> ItemPriority.LOW
            ItemPriority.URGENT.value -> ItemPriority.URGENT
            else -> {
                ItemPriority.NORMAL
            }
        }
    }
}