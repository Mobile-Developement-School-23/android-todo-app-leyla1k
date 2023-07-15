package com.example.todoapp.network

import android.util.Log
import androidx.work.CoroutineWorker
import com.example.todoapp.TodoApplication


import android.content.Context;

import androidx.work.WorkerParameters;
import com.example.todoapp.TodoListRepositoryImpl

private const val TAG = "SynchronizeWorker"

class SynchronizeWorker(ctx:Context, params:WorkerParameters) : CoroutineWorker(ctx, params) {

    override suspend fun doWork(): Result {
        return try {
            val repo = TodoApplication.getInstance().todoListRepositoryImpl
            repo.refreshData()
            Result.success()
        } catch (throwable: Throwable) {
            Log.d(TAG, "Error synchronize data")
            Result.failure()
        }
    }

}