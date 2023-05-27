package ru.nsu.group20211.airport_system.data.flight

import entity.addJoins
import entity.addOrderBy
import entity.addWhere
import entity.log
import ru.nsu.group20211.airport_system.data.Repository
import ru.nsu.group20211.airport_system.di.app_module.DatabaseModule
import ru.nsu.group20211.airport_system.domain.employee.models.Brigade
import ru.nsu.group20211.airport_system.domain.employee.models.Brigade.Companion.getInstance
import ru.nsu.group20211.airport_system.domain.flights.models.FlightSchedule
import ru.nsu.group20211.airport_system.domain.flights.models.FlightSchedule.Companion.getInstance
import ru.nsu.group20211.airport_system.domain.flights.models.TechnicalInspection
import ru.nsu.group20211.airport_system.domain.flights.models.TechnicalInspection.Companion.getInstance
import javax.inject.Inject

class TechnicalInspectionRepository @Inject constructor(
    override val dbContainer: DatabaseModule.DriverContainer
) : Repository<TechnicalInspection> {
    override suspend fun getAll(
        listCond: List<String>,
        listOrder: List<String>
    ): List<TechnicalInspection> {
        val listJoins = listOf(
            """ LEFT JOIN ${Brigade.getTableName()} ON (${Brigade.getTableName()}."id" = ${TechnicalInspection.getTableName()}."idInspectionTeam") """,
            """ LEFT JOIN ${FlightSchedule.getTableName()} ON (${FlightSchedule.getTableName()}."id" = ${TechnicalInspection.getTableName()}."idFlight") """,
        )
        val listResult = mutableListOf<TechnicalInspection>()
        return dbContainer.connect().use {
            val result = it.executeQuery(
                (TechnicalInspection.getAll() + addJoins(listJoins) + addWhere(listCond) +
                        addOrderBy(listOrder)).log()
            )
            while (result.next()) {
                val (inspection, index) = result.getInstance(clazz = TechnicalInspection::class)
                val (brigade, index_2) = result.getInstance(index, clazz = Brigade::class)
                val (schedule, index_3) = result.getInstance(index_2, clazz = FlightSchedule::class)
                inspection.brigade = brigade
                inspection.schedule = schedule
                listResult.add(inspection)
            }
            listResult
        }
    }
}