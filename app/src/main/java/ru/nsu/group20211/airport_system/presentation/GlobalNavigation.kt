package ru.nsu.group20211.airport_system.presentation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.nsu.group20211.airport_system.presentation.plane.Large_technical_inspection.LargeTechnicalInspectionFragment
import ru.nsu.group20211.airport_system.presentation.plane.aircraft_repair_report.AircraftRepairReportFragment
import ru.nsu.group20211.airport_system.presentation.plane.model_plane.ModelPlaneFragment
import ru.nsu.group20211.airport_system.presentation.plane.plane.PlaneFragment
import ru.nsu.group20211.airport_system.presentation.worker.brigades.BrigadeFragment
import ru.nsu.group20211.airport_system.presentation.worker.department.DepartmentFragment
import ru.nsu.group20211.airport_system.presentation.worker.employee.EmployeeFragment
import ru.nsu.group20211.airport_system.presentation.worker.employee_class.EmployeeClassFragment
import ru.nsu.group20211.airport_system.presentation.worker.human.HumanFragment

object GlobalNavigation {
    fun EmployeeScreen() = FragmentScreen("EmployeeScreen", false) {
        EmployeeFragment.newInstance()
    }

    fun HumanScreen() = FragmentScreen("HumanScreen", false) {
        HumanFragment.newInstance()
    }

    fun BrigadeScreen() = FragmentScreen("BrigadeScreen", false) {
        BrigadeFragment.newInstance()
    }

    fun EmployeeClassScreen() = FragmentScreen("EmployeeClassScreen", false) {
        EmployeeClassFragment.newInstance()
    }

    fun DepartmentScreen() = FragmentScreen("DepartmentScreen", false) {
        DepartmentFragment.newInstance()
    }

    fun AircraftRepairReportScreen() = FragmentScreen("AircraftRepairReportScreen", false) {
        AircraftRepairReportFragment.newInstance()
    }

    fun LargeTechnicalInspectionScreen() = FragmentScreen("LargeTechnicalInspectionScreen", false) {
        LargeTechnicalInspectionFragment.newInstance()
    }

    fun ModelPlaneScreen() = FragmentScreen("ModelPlaneScreen", false) {
        ModelPlaneFragment.newInstance()
    }

    fun PlaneScreen() = FragmentScreen("PlaneScreen", false) {
        PlaneFragment.newInstance()
    }

}