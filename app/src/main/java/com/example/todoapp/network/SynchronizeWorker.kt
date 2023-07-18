package com.example.todoapp.network

import android.util.Log
import androidx.work.CoroutineWorker
import com.example.todoapp.TodoApplication


import android.content.Context;

import androidx.work.WorkerParameters;
import com.example.todoapp.TodoListRepositoryImpl
import com.example.todoapp.storage.repository.TodoListRepository
import javax.inject.Inject

private const val TAG = "SynchronizeWorker"

class SynchronizeWorker @Inject constructor(
    ctx:Context,
    params:WorkerParameters,
    private val repo: TodoListRepository
) : CoroutineWorker(ctx, params) {

    override suspend fun doWork(): Result {
        return try {
            repo.refreshData()
            Result.success()
        } catch (throwable: Throwable) {
            Log.d(TAG, "Error synchronize data")
            Result.failure()
        }
    }

}