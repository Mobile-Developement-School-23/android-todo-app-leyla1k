package com.example.todoapp


import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.todoapp.R
import okhttp3.internal.notify


class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
      /*  val text = intent.getStringExtra(TITLE)
        val importance = intent.getStringExtra(IMPORTANCE)
        if (text != null && importance != null) {
            notification(context, text, importance)
        }*/
    }
/*
    private fun notification(context: Context, text : String, importance: String) {
        val notificationManager =
            ContextCompat.getSystemService(context, NotificationManager::class.java)
        val importanceNew = customImportance(context, importance)
        val notification = NotificationCompat.Builder(context, TodoApplication.CHANNEL_ID)
            .setContentTitle(context.getString(R.string.title_text) + " " + importanceNew.lowercase())
            .setContentText(text)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()
        notificationManager?.notify(1, notification)
    }

    private fun customImportance(context: Context, importance: String): String {
        return when (importance) {
            "BASIC" -> context.getString(R.string.importance_basic)
            "IMPORTANT" -> context.getString(R.string.importance_high)
            "LOW" -> context.getString(R.string.importance_low)
            else -> context.getString(R.string.importance_basic)
        }
    }

    companion object {
        const val TITLE = "text"
        const val IMPORTANCE = "importance"
    }*/
}