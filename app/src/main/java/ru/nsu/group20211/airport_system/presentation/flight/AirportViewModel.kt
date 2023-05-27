package ru.nsu.group20211.airport_system.presentation.flight

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.nsu.group20211.airport_system.data.Repository
import ru.nsu.group20211.airport_system.domain.flights.models.Airport
import ru.nsu.group20211.airport_system.presentation.BaseDbViewModel

class AirportViewModel(
    override val repository: Repository<Airport>
) : BaseDbViewModel<Airport>() {

    override val stateProvider = MutableStateFlow<List<Airport>>(emptyList())
    override val errorProvider = MutableSharedFlow<Throwable>()
}