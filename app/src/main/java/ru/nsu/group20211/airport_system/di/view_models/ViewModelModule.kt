package ru.nsu.group20211.airport_system.di.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.nsu.group20211.airport_system.presentation.employee.EmployeeViewModel
import ru.nsu.group20211.airport_system.presentation.human.HumanViewModel

@Module(includes = [ViewModelBinds::class])
class ViewModelModule {

}

@Module
interface ViewModelBinds {

    @Binds
    @IntoMap
    @ViewModelKey(EmployeeViewModel::class)
    fun bindEmployeeViewModel(editPlaceViewModel: EmployeeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HumanViewModel::class)
    fun bindHumanViewModel(editPlaceViewModel: HumanViewModel): ViewModel

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}