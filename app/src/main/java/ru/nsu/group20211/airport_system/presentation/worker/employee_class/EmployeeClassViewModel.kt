package ru.nsu.group20211.airport_system.presentation.worker.employee_class

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.nsu.group20211.airport_system.data.employee.EmployeeClassRepository
import ru.nsu.group20211.airport_system.domain.employee.models.Employee
import ru.nsu.group20211.airport_system.domain.employee.models.EmployeeClass
import ru.nsu.group20211.airport_system.presentation.BaseDbViewModel
import javax.inject.Inject

class EmployeeClassViewModel @Inject constructor(
    override val repository: EmployeeClassRepository
) : BaseDbViewModel<EmployeeClass>() {

    override val stateProvider = MutableStateFlow<List<EmployeeClass>>(emptyList())
    override val errorProvider = MutableSharedFlow<Throwable>()

    suspend fun getEmployees(): List<Employee> {
        return repository.getEmployees()
    }
}