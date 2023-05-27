package ru.nsu.group20211.airport_system.presentation.worker.employee

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.nsu.group20211.airport_system.data.employee.DepartmentRepository
import ru.nsu.group20211.airport_system.data.employee.EmployeeRepository
import ru.nsu.group20211.airport_system.data.employee.HumanRepository
import ru.nsu.group20211.airport_system.domain.employee.models.Brigade
import ru.nsu.group20211.airport_system.domain.employee.models.Department
import ru.nsu.group20211.airport_system.domain.employee.models.Employee
import ru.nsu.group20211.airport_system.domain.employee.models.Human
import ru.nsu.group20211.airport_system.presentation.BaseDbViewModel
import ru.nsu.group20211.airport_system.runCatchingNonCancellation
import javax.inject.Inject

class EmployeeViewModel @Inject constructor(
    override val repository: EmployeeRepository,
    private val humanRepository: HumanRepository,
    private val departmentRepository: DepartmentRepository
) : BaseDbViewModel<Employee>() {

    override val stateProvider = MutableStateFlow<List<Employee>>(emptyList())
    override val errorProvider = MutableSharedFlow<Throwable>()

    suspend fun getHumans(): List<Human> {
        return runCatchingNonCancellation {
            humanRepository.getAll()
        }.getOrElse {
            it.printStackTrace()
            errorProvider.emit(it)
            emptyList()
        }
    }

    suspend fun getBrigades(): List<Brigade> {
        return runCatchingNonCancellation {
            repository.getBrigades()
        }.getOrElse {
            it.printStackTrace()
            errorProvider.emit(it)
            emptyList()
        }
    }

    suspend fun getMinSalary(): Float {
        return runCatchingNonCancellation {
            repository.getMinSalary()
        }.getOrElse {
            it.printStackTrace()
            errorProvider.emit(it)
            0F
        }
    }

    suspend fun getMaxSalary(): Float {
        return runCatchingNonCancellation {
            repository.getMaxSalary()
        }.getOrElse {
            it.printStackTrace()
            errorProvider.emit(it)
            Float.MAX_VALUE
        }
    }

    suspend fun getMinExperience(): Int {
        return runCatchingNonCancellation {
            repository.getMinExperience()
        }.getOrElse {
            it.printStackTrace()
            errorProvider.emit(it)
            0
        }
    }

    suspend fun getMaxExperience(): Int {
        return runCatchingNonCancellation {
            repository.getMaxExperience()
        }.getOrElse {
            it.printStackTrace()
            errorProvider.emit(it)
            Int.MAX_VALUE
        }
    }

    suspend fun getCount(): Int {
        return runCatchingNonCancellation {
            repository.getCountEmployees()
        }.getOrElse {
            it.printStackTrace()
            errorProvider.emit(it)
            0
        }
    }

    suspend fun getDepartment(): List<Department> {
        return runCatchingNonCancellation {
            departmentRepository.getAll()
        }.getOrElse {
            it.printStackTrace()
            errorProvider.emit(it)
            emptyList()
        }
    }
}