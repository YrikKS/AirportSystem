package ru.nsu.group20211.airport_system.presentation.flight.type_fule

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.nsu.group20211.airport_system.data.flight.TypeFuleRepository
import ru.nsu.group20211.airport_system.domain.flights.models.TypeFule
import ru.nsu.group20211.airport_system.presentation.BaseDbViewModel
import javax.inject.Inject

class TypeFuleViewModel @Inject constructor(
    override val repository: TypeFuleRepository
) : BaseDbViewModel<TypeFule>() {

    override val stateProvider = MutableStateFlow<List<TypeFule>>(emptyList())
    override val errorProvider = MutableSharedFlow<Throwable>()
}