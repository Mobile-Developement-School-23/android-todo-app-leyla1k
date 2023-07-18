package com.example.todoapp.di.modules

import androidx.lifecycle.ViewModel
import com.example.todoapp.ui.viewmodels.AddTodoViewModel
import com.example.todoapp.ui.viewmodels.EditTodoViewModel
import com.example.todoapp.ui.viewmodels.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddTodoViewModel::class)
    internal abstract fun bindAddTodoViewModel(addTodoViewModel: AddTodoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditTodoViewModel::class)
    internal abstract fun bindEditTodoViewModel(editTodoViewModel: EditTodoViewModel): ViewModel

//    @Binds
//    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}