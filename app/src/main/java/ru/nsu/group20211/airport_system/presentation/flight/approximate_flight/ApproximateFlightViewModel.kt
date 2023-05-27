package ru.nsu.group20211.airport_system.presentation.flight.approximate_flight

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.nsu.group20211.airport_system.data.flight.AirportRepository
import ru.nsu.group20211.airport_system.data.flight.ApproximateFlightRepository
import ru.nsu.group20211.airport_system.domain.flights.models.Airport
import ru.nsu.group20211.airport_system.domain.flights.models.ApproximateFlight
import ru.nsu.group20211.airport_system.presentation.BaseDbViewModel
import ru.nsu.group20211.airport_system.runCatchingNonCancellation
import javax.inject.Inject

class ApproximateFlightViewModel @Inject constructor(
    override val repository: ApproximateFlightRepository,
    private val airportRepository: AirportRepository
) : BaseDbViewModel<ApproximateFlight>() {
    override val stateProvider = MutableStateFlow<List<ApproximateFlight>>(emptyList())
    override val errorProvider = MutableSharedFlow<Throwable>()


    suspend fun getAirports(): List<Airport> {
        return runCatchingNonCancellation {
            airportRepository.getAll()
        }.getOrElse {
            it.printStackTrace()
            errorProvider.emit(it)
            emptyList()
        }
    }
}