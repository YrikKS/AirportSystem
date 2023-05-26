package ru.nsu.group20211.airport_system.di.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.nsu.group20211.airport_system.presentation.brigades.BrigadeViewModel
import ru.nsu.group20211.airport_system.presentation.employee.EmployeeViewModel
import ru.nsu.group20211.airport_system.presentation.employee_class.EmployeeClassViewModel
import ru.nsu.group20211.airport_system.presentation.human.HumanViewModel

@Module(includes = [ViewModelBinds::class])
class ViewModelModule {

}

@Module
interface ViewModelBinds {

    @Binds
    @IntoMap
    @ViewModelKey(EmployeeViewModel::class)
    fun bindEmployeeViewModel(employeeViewModel: EmployeeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HumanViewModel::class)
    fun bindHumanViewModel(humanViewModel: HumanViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BrigadeViewModel::class)
    fun bindBrigadeViewModel(brigadeViewModel: BrigadeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EmployeeClassViewModel::class)
    fun bindEmployeeClassViewModel(employeeClassViewModel: EmployeeClassViewModel): ViewModel

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}