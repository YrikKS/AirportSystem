package ru.nsu.group20211.airport_system.presentation.human

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.nsu.group20211.airport_system.data.EmployeeRepository
import ru.nsu.group20211.airport_system.data.HumanRepository
import ru.nsu.group20211.airport_system.domain.models.DbEntity
import ru.nsu.group20211.airport_system.domain.models.Human
import ru.nsu.group20211.airport_system.presentation.BaseDbViewModel
import ru.nsu.group20211.airport_system.runCatchingNonCancellation
import javax.inject.Inject

class HumanViewModel @Inject constructor(
    private val repository: HumanRepository
) : ViewModel(), BaseDbViewModel {

    val stateProvider = MutableStateFlow<List<Human>>(emptyList())
    val errorProvider = MutableSharedFlow<Throwable>()

    override fun getData(
        listCond: List<String>,
        listOrder: List<String>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatchingNonCancellation {
                repository.getHumans(listCond, listOrder)
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
                repository.deleteHuman(dbEntity as Human)
                repository.getHumans()
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
                repository.insertHuman(dbEntity as Human)
                repository.getHumans()
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
                repository.updateHuman(dbEntity as Human)
                repository.getHumans()
            }.onFailure {
                errorProvider.emit(it)
            }.onSuccess {
                stateProvider.emit(it)
            }
        }
    }
}