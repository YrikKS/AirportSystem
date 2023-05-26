package ru.nsu.group20211.airport_system.presentation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.nsu.group20211.airport_system.presentation.brigades.BrigadeFragment
import ru.nsu.group20211.airport_system.presentation.employee.EmployeeFragment
import ru.nsu.group20211.airport_system.presentation.employee_class.EmployeeClassFragment
import ru.nsu.group20211.airport_system.presentation.human.HumanFragment

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
}