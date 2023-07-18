package com.example.todoapp.di.editTodo

import com.example.todoapp.di.TodoScope
import com.example.todoapp.ui.fragments.EditTodoFragment
import dagger.Subcomponent

@TodoScope
@Subcomponent
interface EditTodoComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): EditTodoComponent
    }
    fun inject(editTodoFragment: EditTodoFragment)
}