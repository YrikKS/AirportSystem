package ru.nsu.group20211.airport_system.data.plane

import entity.addOrderBy
import entity.addWhere
import entity.log
import ru.nsu.group20211.airport_system.data.Repository
import ru.nsu.group20211.airport_system.di.app_module.DatabaseModule
import ru.nsu.group20211.airport_system.domain.plane.models.ModelPlane
import ru.nsu.group20211.airport_system.domain.plane.models.ModelPlane.Companion.getInstance
import javax.inject.Inject

class ModelPlaneRepository @Inject constructor(
    override val dbContainer: DatabaseModule.DriverContainer
) : Repository<ModelPlane> {

    override suspend fun getAll(listCond: List<String>, listOrder: List<String>): List<ModelPlane> {
        return dbContainer.connect().use {
            val resultList = mutableListOf<ModelPlane>()
            val result =
                it.executeQuery((ModelPlane.getAll() + addWhere(listCond) + addOrderBy(listOrder)).log())
            while (result.next()) {
                resultList.add(result.getInstance(clazz = ModelPlane::class).first)
            }
            resultList
        }
    }
}