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

        with(viewHolder.binding) {
            tvMsg.text = item.msg  // Текст туду

            when (item.priority) { // Текст приоритета
                ItemPriority.LOW -> {
                    ivPriority.setImageResource(R.drawable.ic_low_priority)
                    ivPriority.visibility = View.VISIBLE

                    if (!item.isCompleted){
                        checkboxDone.isErrorShown = false
                    }
                }
                ItemPriority.NORMAL -> {
                    ivPriority.visibility = View.GONE
                    if (!item.isCompleted){
                        checkboxDone.isErrorShown = false
                    }
                }

                ItemPriority.URGENT -> {
                    ivPriority.setImageResource(R.drawable.ic_urgent_priority)
                    ivPriority.visibility = View.VISIBLE
                    if (!item.isCompleted){
                        checkboxDone.isErrorShown = true
                    }
                }
            }

            if (item.deadline != null){
                val calendar = Calendar.getInstance()
                calendar.time = item.deadline!!
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val month = calendar.get(Calendar.MONTH) + 1
                val year = calendar.get(Calendar.YEAR)

                tvDate.visibility = View.VISIBLE
                tvDate.text = root.context.getString(R.string.date, day, month, year)
            }else{
                tvDate.visibility = View.GONE
            }


            if (item.isCompleted){
                setTodoCompleted(checkboxDone, tvMsg)
            }else{
                setTodoNotCompleted(checkboxDone, item, tvMsg)
            }

            container.setOnClickListener {
                onTodoItemClickListener?.invoke(item)
            }

            checkboxDone.setOnClickListener {
                onCheckBoxClickListener?.invoke(item)
            }





           /* checkboxDone.setOnClickListener {
                checkboxDone.isErrorShown = false
                if (checkboxDone.checkedState == MaterialCheckBox.STATE_CHECKED) {
                    setTodoCompleted(checkboxDone, tvMsg)
                    item.isCompleted=true
                   // MainFragment.countDone.value = MainFragment.countDone.value?.plus(1) // переделаю
                }else{
                   // MainFragment.countDone.value = MainFragment.countDone.value?.minus(1)
                    setTodoNotCompleted(checkboxDone, item, tvMsg)
                    item.isCompleted=false
                }
            }*/





        }
    }

    private fun setTodoCompleted(box: MaterialCheckBox, tv: MaterialTextView){
        box.checkedState = MaterialCheckBox.STATE_CHECKED


        tv.setTextAppearance(R.style.TextViewRCompleted)
        box.isErrorShown = false

        tv.paintFlags =  Paint.STRIKE_THRU_TEXT_FLAG
    }

    private fun setTodoNotCompleted(box: MaterialCheckBox, item: TodoItem, tv: MaterialTextView){
        box.checkedState = MaterialCheckBox.STATE_UNCHECKED

        box.isErrorShown = item.priority == ItemPriority.URGENT

        tv.setTextAppearance(R.style.TextViewR)


        tv.paintFlags = 0

    }
}