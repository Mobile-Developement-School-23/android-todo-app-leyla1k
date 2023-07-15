package com.example.todoapp.localbase

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "revision")
data class DbRevision(
    @PrimaryKey @NonNull @ColumnInfo(name = "id")
    val id: Int ,
    @NonNull @ColumnInfo(name = "value")
    var value: Int,
)
