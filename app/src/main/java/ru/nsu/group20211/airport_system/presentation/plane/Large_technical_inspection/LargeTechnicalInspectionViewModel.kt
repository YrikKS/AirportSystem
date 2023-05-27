package ru.nsu.group20211.airport_system.presentation.plane.Large_technical_inspection

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.nsu.group20211.airport_system.data.plane.LargeTechnicalInspectionRepository
import ru.nsu.group20211.airport_system.domain.employee.models.Brigade
import ru.nsu.group20211.airport_system.domain.plane.models.LargeTechnicalInspection
import ru.nsu.group20211.airport_system.domain.plane.models.Plane
import ru.nsu.group20211.airport_system.presentation.BaseDbViewModel
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
}
