package ru.nsu.group20211.airport_system.presentation.plane.aircraft_repair_report

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.nsu.group20211.airport_system.data.plane.AircraftRepairReportRepository
import ru.nsu.group20211.airport_system.domain.employee.models.Brigade
import ru.nsu.group20211.airport_system.domain.plane.models.AircraftRepairReport
import ru.nsu.group20211.airport_system.domain.plane.models.Plane
import ru.nsu.group20211.airport_system.presentation.BaseDbViewModel
import javax.inject.Inject

class AircraftRepairReportViewModel @Inject constructor(
    override val repository: AircraftRepairReportRepository
) : BaseDbViewModel<AircraftRepairReport>() {

    override val stateProvider = MutableStateFlow<List<AircraftRepairReport>>(emptyList())
    override val errorProvider = MutableSharedFlow<Throwable>()


    suspend fun getPlanes(): List<Plane> {
        return repository.getPlane()
    }

    suspend fun getBrigades() : List<Brigade> {
        return repository.getBrigade()
    }
}
