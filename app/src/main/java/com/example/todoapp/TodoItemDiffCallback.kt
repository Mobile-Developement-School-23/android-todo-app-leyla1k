package com.example.todoapp

import androidx.recyclerview.widget.DiffUtil
import com.example.todoapp.retrofit.TodoItem

class TodoItemDiffCallback : DiffUtil.ItemCallback<TodoItem>() {

    override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem) = oldItem == newItem
}