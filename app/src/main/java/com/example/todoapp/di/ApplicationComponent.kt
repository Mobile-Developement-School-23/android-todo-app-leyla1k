package com.example.todoapp.di

import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment
import com.example.todoapp.TodoApplication
import com.example.todoapp.di.addTodo.AddTodoItemComponent
import com.example.todoapp.di.editTodo.EditTodoComponent
import com.example.todoapp.di.modules.DBModule
import com.example.todoapp.di.modules.RepositoryModule
import com.example.todoapp.di.modules.TodoApiModule
import com.example.todoapp.di.modules.ViewModelModule
import com.example.todoapp.di.modules.WorkManagerModule
import com.example.todoapp.notifications.AlarmReceiver
import com.example.todoapp.ui.activities.MainActivity
import com.example.todoapp.ui.fragments.MainFragment
import com.example.todoapp.ui.viewmodels.ViewModelFactory
import dagger.BindsInstance
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
annotation class AppContext
@Singleton
@Component(modules = [
    DBModule::class,
    RepositoryModule::class,
    TodoApiModule::class,
    WorkManagerModule::class,
    ViewModelModule::class,
    ApplicationSubcomponents::class,
   ])
interface ApplicationComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application,
                   @BindsInstance  @AppContext context: Context,
                   @BindsInstance retrofit: Retrofit): ApplicationComponent
    }

    fun addTodoItemComponent(): AddTodoItemComponent.Factory
    fun editTodoItemComponent(): EditTodoComponent.Factory

    fun inject(fragment: MainFragment)
    fun inject(activity: MainActivity)
    fun inject(alarmReceiver: AlarmReceiver)

    fun viewModelFactory(): ViewModelFactory
}

fun Fragment.getAppComponent(): ApplicationComponent =
    (requireContext() as TodoApplication).applicationComponent