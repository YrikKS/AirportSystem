package ru.nsu.group20211.airport_system.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.nsu.group20211.airport_system.data.Repository
import ru.nsu.group20211.airport_system.domain.DbEntity
import ru.nsu.group20211.airport_system.runCatchingNonCancellation

abstract class BaseDbViewModel<T : DbEntity> : ViewModel() {
    abstract val repository: Repository<T>
    abstract val stateProvider: MutableStateFlow<List<T>>
    abstract val errorProvider: MutableSharedFlow<Throwable>

    open fun getData(
        listCond: List<String> = emptyList(),
        listOrder: List<String> = emptyList()
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

    fun delete(dbEntity: DbEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatchingNonCancellation {
                repository.delete(dbEntity as T)
                repository.getAll()
            }.onFailure {
                errorProvider.emit(it)
            }.onSuccess {
                stateProvider.emit(it)
            }
        }
    }

    fun insert(dbEntity: DbEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatchingNonCancellation {
                repository.insert(dbEntity as T)
                repository.getAll()
            }.onFailure {
                errorProvider.emit(it)
            }.onSuccess {
                stateProvider.emit(it)
            }
        }
    }

    fun update(dbEntity: DbEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatchingNonCancellation {
                repository.update(dbEntity as T)
                repository.getAll()
            }.onFailure {
                errorProvider.emit(it)
            }.onSuccess {
                stateProvider.emit(it)
            }
        }
    }
}