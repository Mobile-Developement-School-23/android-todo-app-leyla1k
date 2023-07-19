package com.example.todoapp.ui.adapters

import android.content.res.Resources
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.todoapp.ItemPriority
import com.example.todoapp.R
import com.example.todoapp.TodoItem
import com.example.todoapp.ui.rv.TodoItemDiffCallback
import com.example.todoapp.ui.rv.TodoItemViewHolder
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textview.MaterialTextView
import com.example.todoapp.databinding.ItemTodoBinding
import com.example.todoapp.ui.fragments.MainFragment
import java.util.*
import javax.inject.Singleton

@Singleton
class TodoListAdapter : ListAdapter<TodoItem, TodoItemViewHolder>(TodoItemDiffCallback()) {

    fun submit(list:  List<TodoItem>) {
        for( i in list){
            println(i)
        }
        submitList(list)
    }

    var onTodoItemClickListener: ((TodoItem) -> Unit)? = null

    var onCheckBoxClickListener: ((TodoItem) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoItemViewHolder {
        val binding =
            ItemTodoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return TodoItemViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: TodoItemViewHolder, position: Int) {
        val item = getItem(position)
        viewHolder.onBind(item)
        with(viewHolder.binding) {
            container.setOnClickListener {
                onTodoItemClickListener?.invoke(item)
            }
            checkboxDone.setOnClickListener {
                onCheckBoxClickListener?.invoke(item)
            }
        }
    }
}