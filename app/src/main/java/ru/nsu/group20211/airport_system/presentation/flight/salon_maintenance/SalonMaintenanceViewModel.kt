package ru.nsu.group20211.airport_system.presentation.flight.salon_maintenance

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.nsu.group20211.airport_system.data.employee.BrigadeRepository
import ru.nsu.group20211.airport_system.data.flight.FlightScheduleRepository
import ru.nsu.group20211.airport_system.data.flight.SalonMaintenanceRepository
import ru.nsu.group20211.airport_system.domain.employee.models.Brigade
import ru.nsu.group20211.airport_system.domain.flights.models.FlightSchedule
import ru.nsu.group20211.airport_system.domain.flights.models.SalonMaintenance
import ru.nsu.group20211.airport_system.presentation.BaseDbViewModel
import ru.nsu.group20211.airport_system.runCatchingNonCancellation
import javax.inject.Inject

class SalonMaintenanceViewModel @Inject constructor(
    override val repository: SalonMaintenanceRepository,
    private val flightScheduleRepository: FlightScheduleRepository,
    private val brigadesRepository: BrigadeRepository
) : BaseDbViewModel<SalonMaintenance>() {
    override val stateProvider = MutableStateFlow<List<SalonMaintenance>>(emptyList())
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

    suspend fun getBrigades(): List<Brigade> {
        return runCatchingNonCancellation {
            brigadesRepository.getAll()
        }.getOrElse {
            it.printStackTrace()
            errorProvider.emit(it)
            emptyList()
        }
    }
}