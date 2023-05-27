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
import ru.nsu.group20211.airport_system.domain.flights.models.SalonMaintenance
import ru.nsu.group20211.airport_system.domain.flights.models.SalonMaintenance.Companion.getInstance

class SalonMaintenanceRepository(
    override val dbContainer: DatabaseModule.DriverContainer
) : Repository<SalonMaintenance> {
    override suspend fun getAll(
        listCond: List<String>,
        listOrder: List<String>
    ): List<SalonMaintenance> {
        val listJoins = listOf(
            """ LEFT JOIN ${Brigade.getTableName()} ON (${Brigade.getTableName()}."id" = ${SalonMaintenance.getTableName()}."idCleaningTeamSalon") """,
            """ LEFT JOIN ${FlightSchedule.getTableName()} ON (${FlightSchedule.getTableName()}."id" = ${SalonMaintenance.getTableName()}."idFlight") """,
        )
        val listResult = mutableListOf<SalonMaintenance>()
        return dbContainer.connect().use {
            val result = it.executeQuery(
                (SalonMaintenance.getAll() + addJoins(listJoins) + addWhere(listCond) +
                        addOrderBy(listOrder)).log()
            )
            while (result.next()) {
                val (salonMaintenance, index) = result.getInstance(clazz = SalonMaintenance::class)
                val (brigade, index_2) = result.getInstance(index, clazz = Brigade::class)
                val (schedule, index_3) = result.getInstance(index_2, clazz = FlightSchedule::class)
                salonMaintenance.brigade = brigade
                salonMaintenance.schedule = schedule
                listResult.add(salonMaintenance)
            }
            listResult
        }
    }
}