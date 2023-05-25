package ru.nsu.group20211.airport_system.presentation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import ru.nsu.group20211.airport_system.presentation.employee.EmployeeFragment
import ru.nsu.group20211.airport_system.presentation.human.HumanFragment

object GlobalNavigation {
    fun EmployeeScreen() = FragmentScreen("EmployeeScreen", false) {
        EmployeeFragment.newInstance()
    }

    fun HumanScreen() = FragmentScreen("HumanScreen", false) {
        HumanFragment.newInstance()
    }
}