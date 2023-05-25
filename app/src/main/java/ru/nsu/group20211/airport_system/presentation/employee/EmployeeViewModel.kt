package ru.nsu.group20211.airport_system.presentation.employee

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.nsu.group20211.airport_system.data.EmployeeRepository
import ru.nsu.group20211.airport_system.data.HumanRepository
import ru.nsu.group20211.airport_system.domain.models.Brigade
import ru.nsu.group20211.airport_system.domain.models.DbEntity
import ru.nsu.group20211.airport_system.domain.models.Employee
import ru.nsu.group20211.airport_system.domain.models.Human
import ru.nsu.group20211.airport_system.presentation.BaseDbViewModel
import ru.nsu.group20211.airport_system.runCatchingNonCancellation
import javax.inject.Inject

class EmployeeViewModel @Inject constructor(
    private val repository: EmployeeRepository,
    private val humanRepository: HumanRepository
) : ViewModel(), BaseDbViewModel {

    val stateProvider = MutableStateFlow<List<Employee>>(emptyList())
    val errorProvider = MutableSharedFlow<Throwable>()

    override fun getData(
        listCond: List<String>,
        listOrder: List<String>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatchingNonCancellation {
                repository.getEmployees(listCond, listOrder)
            }.onFailure {
                errorProvider.emit(it)
            }.onSuccess {
                stateProvider.emit(it)
            }
        }
    }

    override fun delete(dbEntity: DbEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatchingNonCancellation {
                repository.deleteEmployee(dbEntity as Employee)
                repository.getEmployees()
            }.onFailure {
                errorProvider.emit(it)
            }.onSuccess {
                stateProvider.emit(it)
            }
        }
    }

    override fun insert(dbEntity: DbEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatchingNonCancellation {
                repository.insertEmployee(dbEntity as Employee)
                repository.getEmployees()
            }.onFailure {
                errorProvider.emit(it)
            }.onSuccess {
                stateProvider.emit(it)
            }
        }
    }

    override fun update(dbEntity: DbEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatchingNonCancellation {
                repository.updateEmployee(dbEntity as Employee)
                repository.getEmployees()
            }.onFailure {
                errorProvider.emit(it)
            }.onSuccess {
                stateProvider.emit(it)
            }
        }
    }

    suspend fun getHumans(): List<Human> {
        return humanRepository.getHumans()
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