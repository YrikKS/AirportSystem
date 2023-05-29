package ru.nsu.group20211.airport_system.presentation.tickets.passengers

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.nsu.group20211.airport_system.data.passengers.PassengerRepository
import ru.nsu.group20211.airport_system.domain.passengers.models.Passenger
import ru.nsu.group20211.airport_system.presentation.BaseDbViewModel
import javax.inject.Inject

class PassengerViewModel @Inject constructor(
    override val repository: PassengerRepository
) : BaseDbViewModel<Passenger>() {
    override val stateProvider = MutableStateFlow<List<Passenger>>(emptyList())
    override val errorProvider = MutableSharedFlow<Throwable>()

}