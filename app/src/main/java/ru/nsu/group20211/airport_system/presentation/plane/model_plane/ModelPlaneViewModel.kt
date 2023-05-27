package ru.nsu.group20211.airport_system.presentation.plane.model_plane

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.nsu.group20211.airport_system.data.plane.ModelPlaneRepository
import ru.nsu.group20211.airport_system.domain.plane.models.ModelPlane
import ru.nsu.group20211.airport_system.presentation.BaseDbViewModel
import javax.inject.Inject

class ModelPlaneViewModel @Inject constructor(
    override val repository: ModelPlaneRepository
) : BaseDbViewModel<ModelPlane>() {

    override val stateProvider = MutableStateFlow<List<ModelPlane>>(emptyList())
    override val errorProvider = MutableSharedFlow<Throwable>()
}