package com.example.todoapp.storage.localbase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todoapp.ItemPriority
import java.util.*


@Entity(tableName = "Items")
data class TodoDataItem(

    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "msg")
    var msg: String,

    @ColumnInfo(name = "priority")
    var priority: ItemPriority,

    @ColumnInfo(name = "deadline")
    var deadline: Date?,

    @ColumnInfo(name = "isCompleted")
    var isCompleted: Boolean,

    @ColumnInfo(name = "createDate")
    val createDate: Date,

    @ColumnInfo(name = "changedDate")
    var changedDate: Date?

)
