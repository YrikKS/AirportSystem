package ru.nsu.group20211.airport_system.presentation.flight.flight_schedule

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.nsu.group20211.airport_system.data.employee.BrigadeRepository
import ru.nsu.group20211.airport_system.data.flight.AirportRepository
import ru.nsu.group20211.airport_system.data.flight.ApproximateFlightRepository
import ru.nsu.group20211.airport_system.data.flight.FlightScheduleRepository
import ru.nsu.group20211.airport_system.data.plane.PlaneRepository
import ru.nsu.group20211.airport_system.domain.employee.models.Brigade
import ru.nsu.group20211.airport_system.domain.flights.models.Airport
import ru.nsu.group20211.airport_system.domain.flights.models.ApproximateFlight
import ru.nsu.group20211.airport_system.domain.flights.models.FlightSchedule
import ru.nsu.group20211.airport_system.domain.plane.models.Plane
import ru.nsu.group20211.airport_system.presentation.BaseDbViewModel
import ru.nsu.group20211.airport_system.runCatchingNonCancellation
import java.sql.Timestamp
import javax.inject.Inject

class FlightScheduleViewModel @Inject constructor(
    override val repository: FlightScheduleRepository,
    private val airportRepository: AirportRepository,
    private val approximateFlight: ApproximateFlightRepository,
    private val brigadeRepository: BrigadeRepository,
    private val planeRepository: PlaneRepository,
) : BaseDbViewModel<FlightSchedule>() {
    override val stateProvider = MutableStateFlow<List<FlightSchedule>>(emptyList())
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

    suspend fun getApproximateFlight(): List<ApproximateFlight> {
        return runCatchingNonCancellation {
            approximateFlight.getAll()
        }.getOrElse {
            it.printStackTrace()
            errorProvider.emit(it)
            emptyList()
        }
    }

    suspend fun getBrigades(): List<Brigade> {
        return runCatchingNonCancellation {
            brigadeRepository.getAll()
        }.getOrElse {
            it.printStackTrace()
            errorProvider.emit(it)
            emptyList()
        }
    }

    suspend fun getPlanes(): List<Plane> {
        return runCatchingNonCancellation {
            planeRepository.getAll()
        }.getOrElse {
            it.printStackTrace()
            errorProvider.emit(it)
            emptyList()
        }
    }

    suspend fun getMinPrice(): Float {
        return runCatchingNonCancellation {
            repository.getMinPrice()
        }.getOrElse {
            it.printStackTrace()
            errorProvider.emit(it)
            0F
        }
    }

    suspend fun getMaxPrice(): Float {
        return runCatchingNonCancellation {
            repository.getMaxPrice()
        }.getOrElse {
            it.printStackTrace()
            errorProvider.emit(it)
            Float.MAX_VALUE
        }
    }

    suspend fun getMinTakeOffTime(): Timestamp {
        return runCatchingNonCancellation {
            repository.getMinTimestamp()
        }.getOrElse {
            it.printStackTrace()
            errorProvider.emit(it)
            Timestamp.valueOf("1999-01-01 00:00:00")
        }
    }

    suspend fun getMaxTakeOffTime(): Timestamp {
        return runCatchingNonCancellation {
            repository.getMaxTimestamp()
        }.getOrElse {
            it.printStackTrace()
            errorProvider.emit(it)
            Timestamp.valueOf("2023-01-01 00:00:00")
        }
    }
}