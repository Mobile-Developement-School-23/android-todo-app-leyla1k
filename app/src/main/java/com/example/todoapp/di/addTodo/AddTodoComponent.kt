package com.example.todoapp.di.addTodo


import com.example.todoapp.di.TodoScope
import com.example.todoapp.ui.fragments.AddTodoFragment
import dagger.Subcomponent

@TodoScope
@Subcomponent
interface AddTodoItemComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): AddTodoItemComponent
    }
    fun inject(addTodoFragment: AddTodoFragment)
}