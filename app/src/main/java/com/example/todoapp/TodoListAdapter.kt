package com.example.todoapp

import android.content.res.Resources
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.ListAdapter
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textview.MaterialTextView
import com.example.todoapp.databinding.ItemTodoBinding
import com.example.todoapp.retrofit.ItemPriority
import com.example.todoapp.retrofit.TodoItem
import java.util.*

class TodoListAdapter : ListAdapter<TodoItem, TodoItemViewHolder>(TodoItemDiffCallback()) {

    fun submit(list:  List<TodoItem>) {

        for( i in list){
            println(i)
        }
        Log.d("DEBAG3", list.size.toString())
        submitList(list)
    }

    var onTodoItemClickListener: ((TodoItem) -> Unit)? = null
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

            if (item.deadline != null){ // Дата дедлайна
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


            if (item.isCompleted){ // Состояние чекбокса
               setTodoCompeted(checkboxDone, tvMsg, root.resources)

            }else{
                setTodoNotCompleted(checkboxDone, item, tvMsg, root.resources)
            }

            container.setOnClickListener {  // Клик по итему
                onTodoItemClickListener?.invoke(item)
            }

            checkboxDone.setOnClickListener { // Состояние чекбокса
                checkboxDone.isErrorShown = false
                if (checkboxDone.checkedState == MaterialCheckBox.STATE_CHECKED) {
                    setTodoCompeted(checkboxDone, tvMsg, root.resources)
                    MainFragment.countDone.value = MainFragment.countDone.value?.plus(1) // Потом переделаю на вьюМодель
                }else{
                    MainFragment.countDone.value = MainFragment.countDone.value?.minus(1)
                    setTodoNotCompleted(checkboxDone, item, tvMsg, root.resources)
                }
            }
        }
    }

    private fun setTodoCompeted(box: MaterialCheckBox, tv: MaterialTextView, resources: Resources){
        box.checkedState = MaterialCheckBox.STATE_CHECKED

        tv.setTextColor(
            ResourcesCompat.getColor(resources, R.color.label_tertiary, null)
        )

        box.isErrorShown = false

        tv.paintFlags =  Paint.STRIKE_THRU_TEXT_FLAG
    }

    private fun setTodoNotCompleted(box: MaterialCheckBox, item: TodoItem, tv: MaterialTextView, resources: Resources){
        box.checkedState = MaterialCheckBox.STATE_UNCHECKED

        box.isErrorShown = item.priority == ItemPriority.URGENT

        tv.setTextColor(
            ResourcesCompat.getColor(resources, R.color.label_primary, null)
        )

        tv.paintFlags = 0

    }
}