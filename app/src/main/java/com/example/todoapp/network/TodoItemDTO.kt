package com.example.todoapp.network

import com.google.gson.annotations.SerializedName
import java.util.*


data class TodoItemDto(
    @SerializedName("id")
    val id: UUID,
    @SerializedName("text")
    val msg: String,
    @SerializedName("importance")
    val priority: String,
    @SerializedName("deadline")
    val deadline: Long?,
    @SerializedName("done")
    val isCompleted: Boolean,
    @SerializedName("color")
    val color: String?,
    @SerializedName("created_at")
    val createDate: Long,
    @SerializedName("changed_at")
    val changedDate: Long,
    @SerializedName("last_updated_by")
    val device: String
)
