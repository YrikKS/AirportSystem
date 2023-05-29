package ru.nsu.group20211.airport_system.presentation.flight.type_fule

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.nsu.group20211.airport_system.data.flight.TypeFuelRepository
import ru.nsu.group20211.airport_system.domain.flights.models.TypeFuel
import ru.nsu.group20211.airport_system.presentation.BaseDbViewModel
import javax.inject.Inject

class TypeFuelViewModel @Inject constructor(
    override val repository: TypeFuelRepository
) : BaseDbViewModel<TypeFuel>() {

    override val stateProvider = MutableStateFlow<List<TypeFuel>>(emptyList())
    override val errorProvider = MutableSharedFlow<Throwable>()
}