package ru.nsu.group20211.airport_system.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.nsu.group20211.airport_system.MainActivity
import ru.nsu.group20211.airport_system.di.app_module.AppModule
import ru.nsu.group20211.airport_system.di.app_module.UserModule
import ru.nsu.group20211.airport_system.di.view_models.ViewModelModule
import ru.nsu.group20211.airport_system.presentation.plane.Large_technical_inspection.LargeTechnicalInspectionFragment
import ru.nsu.group20211.airport_system.presentation.plane.aircraft_repair_report.AircraftRepairReportFragment
import ru.nsu.group20211.airport_system.presentation.plane.model_plane.ModelPlaneFragment
import ru.nsu.group20211.airport_system.presentation.plane.plane.PlaneFragment
import ru.nsu.group20211.airport_system.presentation.worker.brigades.BrigadeFragment
import ru.nsu.group20211.airport_system.presentation.worker.department.DepartmentFragment
import ru.nsu.group20211.airport_system.presentation.worker.employee.EmployeeFragment
import ru.nsu.group20211.airport_system.presentation.worker.employee_class.EmployeeClassFragment
import ru.nsu.group20211.airport_system.presentation.worker.human.HumanFragment
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        UserModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent {
    fun inject(target: EmployeeFragment)
    fun inject(target: MainActivity)
    fun inject(target: BrigadeFragment)
    fun inject(target: HumanFragment)
    fun inject(target: EmployeeClassFragment)
    fun inject(target: DepartmentFragment)
    fun inject(target: AircraftRepairReportFragment)
    fun inject(target: LargeTechnicalInspectionFragment)
    fun inject(target: ModelPlaneFragment)
    fun inject(target: PlaneFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance
            context: Context
        ): AppComponent
    }
}