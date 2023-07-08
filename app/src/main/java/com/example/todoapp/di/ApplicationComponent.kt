package com.example.todoapp.di

import android.content.Context
import androidx.core.view.KeyEventDispatcher
import com.example.todoapp.MainFragment
import dagger.BindsInstance
import dagger.Component


@ApplicationScope
@Component(modules = [DBModule::class])
interface ApplicationComponent {

    fun inject(fragment: MainFragment)

  /*  @KeyEventDispatcher.Component.Factory
    interface Factory{
        fun create(
            @BindsInstance context: Context
        ):ApplicationComponent
    }*/
}