package ru.nsu.group20211.airport_system.presentation.flight.refueling

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.nsu.group20211.airport_system.data.Repository
import ru.nsu.group20211.airport_system.data.flight.FlightScheduleRepository
import ru.nsu.group20211.airport_system.data.flight.RefuelingRepository
import ru.nsu.group20211.airport_system.data.flight.TypeFuleRepository
import ru.nsu.group20211.airport_system.domain.flights.models.FlightSchedule
import ru.nsu.group20211.airport_system.domain.flights.models.Refueling
import ru.nsu.group20211.airport_system.domain.flights.models.TypeFule
import ru.nsu.group20211.airport_system.presentation.BaseDbViewModel
import ru.nsu.group20211.airport_system.runCatchingNonCancellation
import javax.inject.Inject

class RefuelingViewModel @Inject constructor(
    override val repository: RefuelingRepository,
    private val typeFuleRepository: TypeFuleRepository,
    private val flightScheduleRepository: FlightScheduleRepository
) : BaseDbViewModel<Refueling>() {
    override val stateProvider = MutableStateFlow<List<Refueling>>(emptyList())
    override val errorProvider = MutableSharedFlow<Throwable>()

    suspend fun getTypeFules(): List<TypeFule> {
        return runCatchingNonCancellation {
            typeFuleRepository.getAll()
        }.getOrElse {
            it.printStackTrace()
            errorProvider.emit(it)
            emptyList()
        }
    }

    suspend fun getSchedule(): List<FlightSchedule> {
        return runCatchingNonCancellation {
            flightScheduleRepository.getAll()
        }.getOrElse {
            it.printStackTrace()
            errorProvider.emit(it)
            emptyList()
        }
    }
}