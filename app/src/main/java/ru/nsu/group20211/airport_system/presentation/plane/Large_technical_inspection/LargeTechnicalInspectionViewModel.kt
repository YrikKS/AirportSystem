package ru.nsu.group20211.airport_system.presentation.plane.Large_technical_inspection

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.nsu.group20211.airport_system.data.plane.LargeTechnicalInspectionRepository
import ru.nsu.group20211.airport_system.domain.employee.models.Brigade
import ru.nsu.group20211.airport_system.domain.plane.models.LargeTechnicalInspection
import ru.nsu.group20211.airport_system.domain.plane.models.Plane
import ru.nsu.group20211.airport_system.presentation.BaseDbViewModel
import ru.nsu.group20211.airport_system.runCatchingNonCancellation
import java.sql.Timestamp
import javax.inject.Inject

class LargeTechnicalInspectionViewModel @Inject constructor(
    override val repository: LargeTechnicalInspectionRepository
) : BaseDbViewModel<LargeTechnicalInspection>() {

    override val stateProvider = MutableStateFlow<List<LargeTechnicalInspection>>(emptyList())
    override val errorProvider = MutableSharedFlow<Throwable>()


    suspend fun getPlanes(): List<Plane> {
        return repository.getPlane()
    }

    suspend fun getBrigades() : List<Brigade> {
        return repository.getBrigade()
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
