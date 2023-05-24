package ru.nsu.group20211.airport_system.di.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.nsu.group20211.airport_system.data.EmployeeRepository
import ru.nsu.group20211.airport_system.presentation.employee.EmployeeViewModel

@Module(includes = [ViewModelBinds::class])
class ViewModelModule {

//    @Provides
//    fun provideEmployeeViewModel(employeeRepository: EmployeeRepository): EmployeeViewModel {
//        return EmployeeViewModel(employeeRepository)
//    }

}

@Module
interface ViewModelBinds {

    @Binds
    @IntoMap
    @ViewModelKey(EmployeeViewModel::class)
    fun bindEditPlaceViewModel(editPlaceViewModel: EmployeeViewModel): ViewModel

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}