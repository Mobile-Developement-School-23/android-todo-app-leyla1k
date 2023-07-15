package com.example.todoapp.di

import com.example.todoapp.ui.MainFragment
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