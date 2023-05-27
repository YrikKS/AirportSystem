package ru.nsu.group20211.airport_system.presentation.plane.aircraft_repair_report

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.nsu.group20211.airport_system.data.plane.AircraftRepairReportRepository
import ru.nsu.group20211.airport_system.domain.employee.models.Brigade
import ru.nsu.group20211.airport_system.domain.plane.models.AircraftRepairReport
import ru.nsu.group20211.airport_system.domain.plane.models.Plane
import ru.nsu.group20211.airport_system.presentation.BaseDbViewModel
import ru.nsu.group20211.airport_system.runCatchingNonCancellation
import java.sql.Date
import java.sql.Timestamp
import javax.inject.Inject

class AircraftRepairReportViewModel @Inject constructor(
    override val repository: AircraftRepairReportRepository
) : BaseDbViewModel<AircraftRepairReport>() {

    override val stateProvider = MutableStateFlow<List<AircraftRepairReport>>(emptyList())
    override val errorProvider = MutableSharedFlow<Throwable>()


    suspend fun getPlanes(): List<Plane> {
        return runCatchingNonCancellation {
            repository.getPlane()
        }.getOrElse {
            it.printStackTrace()
            errorProvider.emit(it)
            emptyList()
        }
    }

    suspend fun getBrigades(): List<Brigade> {
        return runCatchingNonCancellation {
            repository.getBrigade()
        }.getOrElse {
            it.printStackTrace()
            errorProvider.emit(it)
            emptyList()
        }
    }

    suspend fun getMinDate(): Timestamp {
        return runCatchingNonCancellation {
            repository.getMinDate()
        }.getOrElse {
            it.printStackTrace()
            errorProvider.emit(it)
            Timestamp.valueOf("1999-10-10 00:00:00")
        }
    }

    suspend fun getMaxDate(): Timestamp {
        return runCatchingNonCancellation {
            repository.getMaxDate()
        }.getOrElse {
            it.printStackTrace()
            errorProvider.emit(it)
            Timestamp.valueOf("1999-10-10 00:00:00")
        }
    }
}
