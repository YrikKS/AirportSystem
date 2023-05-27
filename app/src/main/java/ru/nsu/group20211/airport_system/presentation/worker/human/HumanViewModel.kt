package ru.nsu.group20211.airport_system.presentation.worker.human

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.nsu.group20211.airport_system.data.employee.HumanRepository
import ru.nsu.group20211.airport_system.domain.employee.models.Human
import ru.nsu.group20211.airport_system.presentation.BaseDbViewModel
import javax.inject.Inject

class HumanViewModel @Inject constructor(
    override val repository: HumanRepository
) : BaseDbViewModel<Human>() {

    override val stateProvider = MutableStateFlow<List<Human>>(emptyList())
    override val errorProvider = MutableSharedFlow<Throwable>()

}