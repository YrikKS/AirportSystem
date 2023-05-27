package ru.nsu.group20211.airport_system.data.plane

import entity.addJoins
import entity.addOrderBy
import entity.addWhere
import entity.log
import ru.nsu.group20211.airport_system.data.Repository
import ru.nsu.group20211.airport_system.di.app_module.DatabaseModule
import ru.nsu.group20211.airport_system.domain.employee.models.Brigade
import ru.nsu.group20211.airport_system.domain.employee.models.Brigade.Companion.getInstance
import ru.nsu.group20211.airport_system.domain.plane.models.AircraftRepairReport
import ru.nsu.group20211.airport_system.domain.plane.models.AircraftRepairReport.Companion.getInstance
import ru.nsu.group20211.airport_system.domain.plane.models.ModelPlane
import ru.nsu.group20211.airport_system.domain.plane.models.ModelPlane.Companion.getInstance
import ru.nsu.group20211.airport_system.domain.plane.models.Plane
import ru.nsu.group20211.airport_system.domain.plane.models.Plane.Companion.getInstance
import javax.inject.Inject

class AircraftRepairReportRepository @Inject constructor(
    override val dbContainer: DatabaseModule.DriverContainer
) : Repository<AircraftRepairReport> {
    override suspend fun getAll(
        listCond: List<String>,
        listOrder: List<String>
    ): List<AircraftRepairReport> {
        val resultList = mutableListOf<AircraftRepairReport>()
        val joinList = listOf(
            """LEFT JOIN ${Plane.getTableName()} ON (${Plane.getTableName()}."id" = "plane") """,
            """LEFT JOIN ${ModelPlane.getTableName()} ON (${ModelPlane.getTableName()}."id" = "model") """,
            """LEFT JOIN ${Brigade.getTableName()} ON (${Brigade.getTableName()}."id" = "repairTeam" )"""
        )
        dbContainer.connect().use {
            val result = it.executeQuery(
                (AircraftRepairReport.getAll() + addJoins(joinList) + addWhere(listCond) + addOrderBy(
                    listOrder
                )).log()
            )
            while (result.next()) {
                val (report, index) = result.getInstance(clazz = AircraftRepairReport::class)
                val (plane, index_2) = result.getInstance(index, clazz = Plane::class)
                val (model, index_3) = result.getInstance(index_2, clazz = ModelPlane::class)
                val (team, index_4) = result.getInstance(index_3, clazz = Brigade::class)
                report.planeEntity = plane
                report.brigade = team
                plane.modelPlane = model
                resultList.add(report)
            }
        }
        return resultList
    }

    suspend fun getPlane(): List<Plane> {
        return dbContainer.connect().use {
            val resultList = mutableListOf<Plane>()
            val joinList = listOf(
                """LEFT JOIN ${ModelPlane.getTableName()} ON (${ModelPlane.getTableName()}."id" = "model") """,
            )
            val result = it.executeQuery((Plane.getAll() + addJoins(joinList)).log())
            while (result.next()) {
                val (plane, index) = result.getInstance(clazz = Plane::class)
                val (model, index_2) = result.getInstance(index, clazz = ModelPlane::class)
                plane.modelPlane = model
                resultList.add(plane)
            }
            resultList
        }
    }

    suspend fun getBrigade(): List<Brigade> {
        return dbContainer.connect().use {
            val resultList = mutableListOf<Brigade>()
            val result = it.executeQuery(Brigade.getAll().log())
            while (result.next()) {
                resultList.add(result.getInstance(clazz = Brigade::class).first)
            }
            resultList
        }
    }
}