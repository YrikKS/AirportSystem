package ru.nsu.group20211.airport_system.presentation.worker.brigades

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.nsu.group20211.airport_system.data.employee.BrigadeRepository
import ru.nsu.group20211.airport_system.data.employee.HumanRepository
import ru.nsu.group20211.airport_system.domain.employee.models.Brigade
import ru.nsu.group20211.airport_system.domain.employee.models.Department
import ru.nsu.group20211.airport_system.presentation.BaseDbViewModel
import ru.nsu.group20211.airport_system.runCatchingNonCancellation
import javax.inject.Inject

class BrigadeViewModel @Inject constructor(
    override val repository: BrigadeRepository,
    private val humanRepository: HumanRepository
) : BaseDbViewModel<Brigade>() {

    override val stateProvider = MutableStateFlow<List<Brigade>>(emptyList())
    override val errorProvider = MutableSharedFlow<Throwable>()

    override fun getData(
        listCond: List<String>,
        listOrder: List<String>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatchingNonCancellation {
                repository.getAll(listCond, listOrder)
            }.onFailure {
                errorProvider.emit(it)
            }.onSuccess {
                stateProvider.emit(it)
            }
        }
    }

    suspend fun getCount(): Int {
        return repository.getCountBrigades()
    }

    suspend fun getDepartment(): List<Department> {
        return repository.getDepartment()
    }
}