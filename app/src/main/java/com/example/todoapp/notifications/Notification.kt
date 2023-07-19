package com.example.todoapp.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.todoapp.di.AppContext
import java.util.Calendar
import javax.inject.Inject


class Notification @Inject constructor(
    @AppContext private val context: Context,
    ) {
        private val alarmManager = context.getSystemService(AlarmManager::class.java)
        private val intent = PendingIntent.getBroadcast(
            context,
            0,
            Intent(context, AlarmReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        fun cancelAlarm() {
            alarmManager.cancel(intent)
        }
        fun scheduleAlarm() {
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 20)
                set(Calendar.MINUTE, 57)
                set(Calendar.SECOND, 0)
            }
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                intent
            )
        }
    }