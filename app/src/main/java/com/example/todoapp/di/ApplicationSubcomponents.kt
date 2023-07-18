package com.example.todoapp.di

import com.example.todoapp.di.addTodo.AddTodoItemComponent
import com.example.todoapp.di.editTodo.EditTodoComponent
import dagger.Component
import dagger.Module

@Module(subcomponents = [AddTodoItemComponent::class, EditTodoComponent::class])
interface ApplicationSubcomponents