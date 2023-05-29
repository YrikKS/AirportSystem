package ru.nsu.group20211.airport_system.data.flight

import ru.nsu.group20211.airport_system.data.addJoins
import ru.nsu.group20211.airport_system.data.addOrderBy
import ru.nsu.group20211.airport_system.data.addWhere
import ru.nsu.group20211.airport_system.data.log
import ru.nsu.group20211.airport_system.data.Repository
import ru.nsu.group20211.airport_system.di.app_module.DatabaseModule
import ru.nsu.group20211.airport_system.domain.employee.models.Brigade
import ru.nsu.group20211.airport_system.domain.employee.models.Brigade.Companion.getInstance
import ru.nsu.group20211.airport_system.domain.flights.models.Airport
import ru.nsu.group20211.airport_system.domain.flights.models.Airport.Companion.getInstance
import ru.nsu.group20211.airport_system.domain.flights.models.FlightSchedule
import ru.nsu.group20211.airport_system.domain.flights.models.FlightSchedule.Companion.getInstance
import ru.nsu.group20211.airport_system.domain.flights.models.Refueling
import ru.nsu.group20211.airport_system.domain.flights.models.Refueling.Companion.getInstance
import ru.nsu.group20211.airport_system.domain.flights.models.TypeFuel
import ru.nsu.group20211.airport_system.domain.flights.models.TypeFuel.Companion.getInstance
import javax.inject.Inject

class RefuelingRepository @Inject constructor(
    override val dbContainer: DatabaseModule.DriverContainer
) : Repository<Refueling> {
    override suspend fun getAll(listCond: List<String>, listOrder: List<String>): List<Refueling> {
        val listJoins = listOf(
            """ LEFT JOIN ${TypeFuel.getTableName()} ON (${TypeFuel.getTableName()}."id" = ${Refueling.getTableName()}."typeFule") """,
            """ LEFT JOIN ${Brigade.getTableName()} ON (${Brigade.getTableName()}."id" = ${Refueling.getTableName()}."idRefuelingTeam") """,
            """ LEFT JOIN ${FlightSchedule.getTableName()} ON (${FlightSchedule.getTableName()}."id" = ${Refueling.getTableName()}."idFlight") """,
            """ LEFT JOIN ${Airport.getTableName()} "departure" ON ("departure"."id" = ${FlightSchedule.getTableName()}."idDepartureAirport") """,
            """ LEFT JOIN ${Airport.getTableName()} "arrival" ON ("arrival"."id" = ${FlightSchedule.getTableName()}."idArrivalAirport") """,
        )
        val listResult = mutableListOf<Refueling>()
        return dbContainer.connect().use {
            val result = it.executeQuery(
                (Refueling.getAll() + addJoins(listJoins) + addWhere(listCond) +
                        addOrderBy(listOrder)).log()
            )
            while (result.next()) {
                val (refueling, index) = result.getInstance(clazz = Refueling::class)
                val (typeFule, index_2) = result.getInstance(index, clazz = TypeFuel::class)
                val (brigade, index_3) = result.getInstance(index_2, clazz = Brigade::class)
                val (schedule, index_4) = result.getInstance(index_3, clazz = FlightSchedule::class)
                val (departure, index_5) = result.getInstance(index_4, clazz = Airport::class)
                val (arrival, index_6) = result.getInstance(index_5, clazz = Airport::class)

                refueling.tupeFule = typeFule
                refueling.refuelingBrigade = brigade
                refueling.schedule = schedule
                schedule.arrival = arrival
                schedule.departure = departure
                listResult.add(refueling)
            }
            listResult
        }

    }
}