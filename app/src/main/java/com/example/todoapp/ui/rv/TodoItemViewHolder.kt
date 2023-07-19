package com.example.todoapp.ui.rv

import android.graphics.Paint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.ItemPriority
import com.example.todoapp.R
import com.example.todoapp.TodoItem
import com.example.todoapp.databinding.ItemTodoBinding
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textview.MaterialTextView
import java.util.Calendar


class TodoItemViewHolder(val binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root){

   fun onBind(item:TodoItem){
      with(this.binding) {
         tvMsg.text = item.msg

         when (item.priority) {
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

            com.example.todoapp.ItemPriority.URGENT -> {
               ivPriority.setImageResource(com.example.todoapp.R.drawable.ic_urgent_priority)
               ivPriority.visibility = View.VISIBLE
               if (!item.isCompleted){
                  checkboxDone.isErrorShown = true
               }
            }
         }

         if (item.deadline != null){
            val calendar = java.util.Calendar.getInstance()
            calendar.time = item.deadline!!
            val day = calendar.get(java.util.Calendar.DAY_OF_MONTH)
            val month = calendar.get(java.util.Calendar.MONTH) + 1
            val year = calendar.get(java.util.Calendar.YEAR)
            tvDate.visibility = android.view.View.VISIBLE
            tvDate.text = root.context.getString(com.example.todoapp.R.string.date, day, month, year)
         }else{
            tvDate.visibility = android.view.View.GONE
         }

         if (item.isCompleted){
            setTodoCompleted(checkboxDone, tvMsg)
         }else{
            setTodoNotCompleted(checkboxDone, item, tvMsg)
         }
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
