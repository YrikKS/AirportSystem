package ru.nsu.group20211.airport_system.presentation.flight.airport

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.nsu.group20211.airport_system.data.flight.AirportRepository
import ru.nsu.group20211.airport_system.domain.flights.models.Airport
import ru.nsu.group20211.airport_system.presentation.BaseDbViewModel
import javax.inject.Inject

class AirportViewModel @Inject constructor(
    override val repository: AirportRepository
) : BaseDbViewModel<Airport>() {

    override val stateProvider = MutableStateFlow<List<Airport>>(emptyList())
    override val errorProvider = MutableSharedFlow<Throwable>()
}