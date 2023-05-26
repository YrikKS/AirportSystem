package ru.nsu.group20211.airport_system.presentation.employee

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.nsu.group20211.airport_system.data.employee.EmployeeRepository
import ru.nsu.group20211.airport_system.data.employee.HumanRepository
import ru.nsu.group20211.airport_system.domain.employee.models.Brigade
import ru.nsu.group20211.airport_system.domain.employee.models.Employee
import ru.nsu.group20211.airport_system.domain.employee.models.Human
import ru.nsu.group20211.airport_system.presentation.BaseDbViewModel
import javax.inject.Inject

class EmployeeViewModel @Inject constructor(
    override val repository: EmployeeRepository,
    private val humanRepository: HumanRepository
) : BaseDbViewModel<Employee>() {

    override val stateProvider = MutableStateFlow<List<Employee>>(emptyList())
    override val errorProvider = MutableSharedFlow<Throwable>()

    suspend fun getHumans(): List<Human> {
        return humanRepository.getAll()
    }

    suspend fun getBrigades(): List<Brigade> {
        return repository.getBrigades()
    }

    suspend fun getMinSalary(): Float {
        return repository.getMinSalary()
    }

    suspend fun getMaxSalary(): Float {
        return repository.getMaxSalary()
    }

    suspend fun getMinExperience(): Int {
        return repository.getMinExperience()
    }

    suspend fun getMaxExperience(): Int {
        return repository.getMaxExperience()
    }

    suspend fun getCount(): Int {
        return repository.getCountEmployees()
    }
}