package ru.nsu.group20211.airport_system.data.plane

import entity.addJoins
import entity.addOrderBy
import entity.addWhere
import entity.log
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import ru.nsu.group20211.airport_system.data.Repository
import ru.nsu.group20211.airport_system.di.app_module.DatabaseModule
import ru.nsu.group20211.airport_system.domain.plane.models.ModelPlane
import ru.nsu.group20211.airport_system.domain.plane.models.ModelPlane.Companion.getInstance
import ru.nsu.group20211.airport_system.domain.plane.models.Plane
import ru.nsu.group20211.airport_system.domain.plane.models.Plane.Companion.getInstance
import javax.inject.Inject

class PlaneRepository @Inject constructor(
    override val dbContainer: DatabaseModule.DriverContainer
) : Repository<Plane> {
    override suspend fun getAll(listCond: List<String>, listOrder: List<String>): List<Plane> {
        val resultList = mutableListOf<Plane>()
        val joinList = listOf(
            """LEFT JOIN ${ModelPlane.getTableName()} ON (${ModelPlane.getTableName()}."id" = "model") """,
        )
        dbContainer.connect().use {
            val result = it.executeQuery(
                (Plane.getAll() + addJoins(joinList) + addWhere(listCond) + addOrderBy(listOrder)).log()
            )
            while (result.next()) {
                val (plane, index) = result.getInstance(clazz = Plane::class)
                val (model, index_2) = result.getInstance(index, clazz = ModelPlane::class)
                plane.modelPlane = model
                resultList.add(plane)
            }
        }
        coroutineScope {
            launch {
                resultList.forEach { plane ->
                    dbContainer.connect().use {
                        val result = it.executeQuery(
                            """ SELECT COUNT(*) AS numberOfTakeoffs
                                    | FROM "planes"
                                    | LEFT JOIN "modelPlane" ON ("modelPlane"."id" = "planes"."model")
                                    | RIGHT JOIN "flightSchedule" ON ("flightSchedule"."plane" = "planes"."id") 
                                    | WHERE "planes"."id" = ${plane.id} """.trimMargin().log()
                        )
                        if (result.next()) {
                            plane.countFlight = result.getInt(1)
                        } else {
                            plane.countFlight = 0
                        }
                    }
                }
            }.join()
        }
        return resultList
    }

    suspend fun getPlaneModel(): List<ModelPlane> {
        return dbContainer.connect().use {
            val resultList = mutableListOf<ModelPlane>()
            val result = it.executeQuery(ModelPlane.getAll().log())
            while (result.next()) {
                resultList.add(result.getInstance(clazz = ModelPlane::class).first)
            }
            resultList
        }
    }
}