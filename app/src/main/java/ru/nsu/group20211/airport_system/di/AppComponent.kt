package ru.nsu.group20211.airport_system.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.nsu.group20211.airport_system.MainActivity
import ru.nsu.group20211.airport_system.di.app_module.AppModule
import ru.nsu.group20211.airport_system.di.app_module.UserModule
import ru.nsu.group20211.airport_system.di.view_models.ViewModelModule
import ru.nsu.group20211.airport_system.presentation.flight.airport.AirportFragment
import ru.nsu.group20211.airport_system.presentation.flight.approximate_flight.ApproximateFlightFragment
import ru.nsu.group20211.airport_system.presentation.flight.flight_schedule.FlightScheduleFragment
import ru.nsu.group20211.airport_system.presentation.flight.refueling.RefuelingFragment
import ru.nsu.group20211.airport_system.presentation.flight.salon_maintenance.SalonMaintenanceFragment
import ru.nsu.group20211.airport_system.presentation.flight.technical_inspection.TechnicalInspectionFragment
import ru.nsu.group20211.airport_system.presentation.flight.type_fule.TypeFuelFragment
import ru.nsu.group20211.airport_system.presentation.main.EmployeeMainFragment
import ru.nsu.group20211.airport_system.presentation.main.PlanesMainFragment
import ru.nsu.group20211.airport_system.presentation.main.ScheduleMainFragment
import ru.nsu.group20211.airport_system.presentation.main.TicketMainFragment
import ru.nsu.group20211.airport_system.presentation.plane.Large_technical_inspection.LargeTechnicalInspectionFragment
import ru.nsu.group20211.airport_system.presentation.plane.aircraft_repair_report.AircraftRepairReportFragment
import ru.nsu.group20211.airport_system.presentation.plane.model_plane.ModelPlaneFragment
import ru.nsu.group20211.airport_system.presentation.plane.plane.PlaneFragment
import ru.nsu.group20211.airport_system.presentation.tickets.passengers.PassengerFragment
import ru.nsu.group20211.airport_system.presentation.tickets.tickets.TicketFragment
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
    fun inject(target: TypeFuelFragment)
    fun inject(target: RefuelingFragment)
    fun inject(target: AirportFragment)
    fun inject(target: ApproximateFlightFragment)
    fun inject(target: SalonMaintenanceFragment)
    fun inject(target: TechnicalInspectionFragment)
    fun inject(target: FlightScheduleFragment)
    fun inject(target: PassengerFragment)
    fun inject(target: TicketFragment)
//    fun inject(target: Fragment)


    fun inject(target: PlanesMainFragment)
    fun inject(target: EmployeeMainFragment)
    fun inject(target: ScheduleMainFragment)
    fun inject(target: TicketMainFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance
            context: Context
        ): AppComponent
    }
}