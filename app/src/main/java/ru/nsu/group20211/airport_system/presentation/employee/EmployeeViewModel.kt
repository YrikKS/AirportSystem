package ru.nsu.group20211.airport_system.presentation.employee

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.nsu.group20211.airport_system.data.EmployeeRepository
import javax.inject.Inject

class EmployeeViewModel @Inject constructor(
    private val employeeRepository: EmployeeRepository
) : ViewModel() {

    fun getSomething() {
        viewModelScope.launch(Dispatchers.IO) {
            employeeRepository.getEmployees(listOf(""" "salary" > 30000 """)).forEach {
                println(it.salary)
            }
        }


//        viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
//            newSuspendedTransaction() {
//                EmployeeEntity.all()
////                Employees.selectAll().first().let { println(it[Employees.salary]) }
////                println(EmployeeEntity.all().first().salary)
//            }
//        }
    }
}