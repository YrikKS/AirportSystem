package ru.nsu.group20211.airport_system.presentation.flight.technical_inspection

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.nsu.group20211.airport_system.data.flight.FlightScheduleRepository
import ru.nsu.group20211.airport_system.data.flight.TechnicalInspectionRepository
import ru.nsu.group20211.airport_system.domain.flights.models.FlightSchedule
import ru.nsu.group20211.airport_system.domain.flights.models.TechnicalInspection
import ru.nsu.group20211.airport_system.presentation.BaseDbViewModel
import ru.nsu.group20211.airport_system.runCatchingNonCancellation
import javax.inject.Inject

class TechnicalInspectionViewModel @Inject constructor(
    override val repository: TechnicalInspectionRepository,
    private val flightScheduleRepository: FlightScheduleRepository
) : BaseDbViewModel<TechnicalInspection>() {
    override val stateProvider = MutableStateFlow<List<TechnicalInspection>>(emptyList())
    override val errorProvider = MutableSharedFlow<Throwable>()

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