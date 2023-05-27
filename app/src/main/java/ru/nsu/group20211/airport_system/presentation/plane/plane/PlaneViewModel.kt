package ru.nsu.group20211.airport_system.presentation.plane.plane

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.nsu.group20211.airport_system.data.plane.PlaneRepository
import ru.nsu.group20211.airport_system.domain.plane.models.ModelPlane
import ru.nsu.group20211.airport_system.domain.plane.models.Plane
import ru.nsu.group20211.airport_system.presentation.BaseDbViewModel
import javax.inject.Inject

class PlaneViewModel @Inject constructor(
    override val repository: PlaneRepository
) : BaseDbViewModel<Plane>() {

    override val stateProvider = MutableStateFlow<List<Plane>>(emptyList())
    override val errorProvider = MutableSharedFlow<Throwable>()

    suspend fun getModelPlane(): List<ModelPlane> {
        return repository.getPlaneModel()
    }
}