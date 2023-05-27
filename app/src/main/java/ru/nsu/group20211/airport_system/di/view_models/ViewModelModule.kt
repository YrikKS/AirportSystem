package ru.nsu.group20211.airport_system.di.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.nsu.group20211.airport_system.presentation.plane.Large_technical_inspection.LargeTechnicalInspectionViewModel
import ru.nsu.group20211.airport_system.presentation.plane.aircraft_repair_report.AircraftRepairReportViewModel
import ru.nsu.group20211.airport_system.presentation.plane.model_plane.ModelPlaneViewModel
import ru.nsu.group20211.airport_system.presentation.plane.plane.PlaneViewModel
import ru.nsu.group20211.airport_system.presentation.worker.brigades.BrigadeViewModel
import ru.nsu.group20211.airport_system.presentation.worker.department.DepartmentViewModel
import ru.nsu.group20211.airport_system.presentation.worker.employee.EmployeeViewModel
import ru.nsu.group20211.airport_system.presentation.worker.employee_class.EmployeeClassViewModel
import ru.nsu.group20211.airport_system.presentation.worker.human.HumanViewModel

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
    @IntoMap
    @ViewModelKey(DepartmentViewModel::class)
    fun bindDepartmentViewModel(departmentViewModel: DepartmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AircraftRepairReportViewModel::class)
    fun bindAircraftRepairReportViewModel(aircraftRepairReportViewModel: AircraftRepairReportViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LargeTechnicalInspectionViewModel::class)
    fun bindLargeTechnicalInspectionViewModel(largeTechnicalInspectionViewModel: LargeTechnicalInspectionViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ModelPlaneViewModel::class)
    fun bindModelPlaneViewModel(modelPlaneViewModel: ModelPlaneViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PlaneViewModel::class)
    fun bindPlaneViewModel(modelPlaneViewModel: PlaneViewModel): ViewModel


    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}