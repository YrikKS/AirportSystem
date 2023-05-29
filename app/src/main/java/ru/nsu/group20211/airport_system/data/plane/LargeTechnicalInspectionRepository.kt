package ru.nsu.group20211.airport_system.data.plane

import ru.nsu.group20211.airport_system.data.addJoins
import ru.nsu.group20211.airport_system.data.addOrderBy
import ru.nsu.group20211.airport_system.data.addWhere
import ru.nsu.group20211.airport_system.data.log
import ru.nsu.group20211.airport_system.data.Repository
import ru.nsu.group20211.airport_system.di.app_module.DatabaseModule
import ru.nsu.group20211.airport_system.domain.employee.models.Brigade
import ru.nsu.group20211.airport_system.domain.employee.models.Brigade.Companion.getInstance
import ru.nsu.group20211.airport_system.domain.plane.models.LargeTechnicalInspection
import ru.nsu.group20211.airport_system.domain.plane.models.LargeTechnicalInspection.Companion.getInstance
import ru.nsu.group20211.airport_system.domain.plane.models.ModelPlane
import ru.nsu.group20211.airport_system.domain.plane.models.ModelPlane.Companion.getInstance
import ru.nsu.group20211.airport_system.domain.plane.models.Plane
import ru.nsu.group20211.airport_system.domain.plane.models.Plane.Companion.getInstance
import java.sql.Timestamp
import javax.inject.Inject

class LargeTechnicalInspectionRepository @Inject constructor(
    override val dbContainer: DatabaseModule.DriverContainer
) : Repository<LargeTechnicalInspection> {
    override suspend fun getAll(
        listCond: List<String>,
        listOrder: List<String>
    ): List<LargeTechnicalInspection> {
        val resultList = mutableListOf<LargeTechnicalInspection>()
        val joinList = listOf(
            """LEFT JOIN ${Plane.getTableName()} ON (${Plane.getTableName()}."id" = "idPlane") """,
            """LEFT JOIN ${ModelPlane.getTableName()} ON (${ModelPlane.getTableName()}."id" = "model") """,
            """LEFT JOIN ${Brigade.getTableName()} ON (${Brigade.getTableName()}."id" = "idInspectionTeam" )"""
        )
        dbContainer.connect().use {
            val result = it.executeQuery(
                (LargeTechnicalInspection.getAll() + addJoins(joinList) + addWhere(listCond) + addOrderBy(
                    listOrder
                )).log()
            )
            while (result.next()) {
                val (report, index) = result.getInstance(clazz = LargeTechnicalInspection::class)
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

    suspend fun getMinDate(): Timestamp {
        return dbContainer.connect().use {
            val result =
                it.executeQuery(""" SELECT MIN("largeTechnicalInspection"."date") FROM "largeTechnicalInspection"""")
            if (result.next()) {
                result.getTimestamp(1)
            } else {
                Timestamp.valueOf("1999-10-10 00:00:00")
            }
        }
    }

    suspend fun getMaxDate(): Timestamp {
        return dbContainer.connect().use {
            val result =
                it.executeQuery(""" SELECT MAX("largeTechnicalInspection"."date") FROM "largeTechnicalInspection"""")
            if (result.next()) {
                result.getTimestamp(1)
            } else {
                Timestamp.valueOf("1999-10-10 00:00:00")
            }
        }
    }
}