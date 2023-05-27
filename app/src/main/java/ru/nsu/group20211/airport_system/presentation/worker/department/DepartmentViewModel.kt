package ru.nsu.group20211.airport_system.presentation.worker.department

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.nsu.group20211.airport_system.data.employee.DepartmentRepository
import ru.nsu.group20211.airport_system.domain.employee.models.Department
import ru.nsu.group20211.airport_system.domain.employee.models.EmployeeClass
import ru.nsu.group20211.airport_system.presentation.BaseDbViewModel
import javax.inject.Inject

class DepartmentViewModel @Inject constructor(
    override val repository: DepartmentRepository
) : BaseDbViewModel<Department>() {
    override val stateProvider = MutableStateFlow<List<Department>>(emptyList())
    override val errorProvider = MutableSharedFlow<Throwable>()

    suspend fun getAdministrators(): List<EmployeeClass.Administrator> {
        return repository.getAdministrators()
    }
}