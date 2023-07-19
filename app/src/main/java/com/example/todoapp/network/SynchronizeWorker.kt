package com.example.todoapp.network

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.util.Log
import androidx.work.CoroutineWorker
import com.example.todoapp.TodoApplication


import android.content.Context;


import androidx.work.WorkerParameters;

import com.example.todoapp.storage.repository.TodoListRepository

import javax.inject.Inject


const val TAG="WORKER_CLASS_SYNC"
class SynchronizeWorker @Inject constructor(
    private val ctx:Context,
    params:WorkerParameters,
    private val repo: TodoListRepository
) : CoroutineWorker(ctx, params) {

    override suspend fun doWork(): Result {
        return try {
            repo.refreshData()

            Result.success()
        } catch (throwable: Throwable) {
            Result.failure()
        }
    }



}