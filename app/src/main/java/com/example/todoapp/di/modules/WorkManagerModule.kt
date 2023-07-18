package com.example.todoapp.di.modules

import android.app.Application
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides

@Module
object WorkManagerModule {
    @Provides
    fun provideWorkManager(app: Application): WorkManager = WorkManager.getInstance(app)
}