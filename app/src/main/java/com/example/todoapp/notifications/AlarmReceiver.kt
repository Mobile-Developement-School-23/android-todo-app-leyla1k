package com.example.todoapp.notifications

import android.R.id.message
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.todoapp.ItemPriority
import com.example.todoapp.TodoApplication
import com.example.todoapp.TodoItem
import com.example.todoapp.storage.repository.TodoListRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject


class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var repo: TodoListRepository

    override fun onReceive(context: Context, intent: Intent) {
        (context as TodoApplication).applicationComponent.inject(this)

        CoroutineScope(Dispatchers.IO).launch {
            val items = getFilteredItems()
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            items.forEach { todo ->
                val importance = when(todo.priority){
                    ItemPriority.LOW -> "Low"
                    ItemPriority.NORMAL -> "Normal"
                    ItemPriority.URGENT -> "Urgent"
                }
                val notification = NotificationCompat.Builder(context, "SuperTodoApplicationChannelId")
                    .setContentTitle("Запланировано")
                    .setContentText("${importance}: ${todo.msg}")
                    .build()
                notificationManager.notify(todo.id.hashCode(), notification)
            }
        }


    }
    private fun getFilteredItems(): List<TodoItem> {
        val dateNow = LocalDate.now()
        return repo.getTodoListAsList()
            .filter {
                val deadline = it.deadline ?: return@filter false
                if(isNotTodoToday(1689767166204, dateNow)){
                    Log.d("KAKSHANotify", "getFilteredItems: ")
                    return@filter false}
                return@filter !it.isCompleted
            }
    }
    private fun isNotTodoToday(timestamp: Long, dateNow: LocalDate): Boolean {
        val date = LocalDate.ofEpochDay(timestamp / (24 * 60 * 60 * 1000))
        return !date.isEqual(dateNow)
    }
}