package ru.nsu.group20211.airport_system.presentation

import com.github.terrakok.cicerone.androidx.FragmentScreen
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

object GlobalNavigation {

    fun EmployeeMainScreen() = FragmentScreen("EmployeeMainScreen", true) {
        EmployeeMainFragment.newInstance()
    }

    fun PlanesMainScreen() = FragmentScreen("PlanesMainScreen", true) {
        PlanesMainFragment.newInstance()
    }

    fun ScheduleMainScreen() = FragmentScreen("ScheduleMainScreen", true) {
        ScheduleMainFragment.newInstance()
    }

    fun TicketMainScreen() = FragmentScreen("TicketMainScreen", true) {
        TicketMainFragment.newInstance()
    }


    fun EmployeeScreen() = FragmentScreen("EmployeeScreen", true) {
        EmployeeFragment.newInstance()
    }

    fun HumanScreen() = FragmentScreen("HumanScreen", true) {
        HumanFragment.newInstance()
    }

    fun BrigadeScreen() = FragmentScreen("BrigadeScreen", true) {
        BrigadeFragment.newInstance()
    }

    fun EmployeeClassScreen() = FragmentScreen("EmployeeClassScreen", true) {
        EmployeeClassFragment.newInstance()
    }

    fun DepartmentScreen() = FragmentScreen("DepartmentScreen", true) {
        DepartmentFragment.newInstance()
    }

    fun AircraftRepairReportScreen() = FragmentScreen("AircraftRepairReportScreen", true) {
        AircraftRepairReportFragment.newInstance()
    }

    fun LargeTechnicalInspectionScreen() = FragmentScreen("LargeTechnicalInspectionScreen", true) {
        LargeTechnicalInspectionFragment.newInstance()
    }

    fun ModelPlaneScreen() = FragmentScreen("ModelPlaneScreen", true) {
        ModelPlaneFragment.newInstance()
    }

    fun PlaneScreen() = FragmentScreen("PlaneScreen", true) {
        PlaneFragment.newInstance()
    }

    fun TypeFuelScreen() = FragmentScreen("TypeFuelScreen", true) {
        TypeFuelFragment.newInstance()
    }

    fun RefuelingScreen() = FragmentScreen("RefuelingScreen", true) {
        RefuelingFragment.newInstance()
    }

    fun AirportScreen() = FragmentScreen("AirportScreen", true) {
        AirportFragment.newInstance()
    }

    fun ApproximateFlightScreen() = FragmentScreen("ApproximateFlightScreen", true) {
        ApproximateFlightFragment.newInstance()
    }

    fun SalonMaintenanceScreen() = FragmentScreen("SalonMaintenanceScreen", true) {
        SalonMaintenanceFragment.newInstance()
    }

    fun TechnicalInspectionScreen() = FragmentScreen("TechnicalInspectionScreen", true) {
        TechnicalInspectionFragment.newInstance()
    }

    fun FlightScheduleScreen() = FragmentScreen("FlightScheduleScreen", true) {
        FlightScheduleFragment.newInstance()
    }

    fun PassengerScreen() = FragmentScreen("PassengerScreen", true) {
        PassengerFragment.newInstance()
    }

    fun TicketScreen() = FragmentScreen("TicketScreen", true) {
        TicketFragment.newInstance()
    }
}