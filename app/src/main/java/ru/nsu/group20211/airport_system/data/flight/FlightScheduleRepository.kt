package ru.nsu.group20211.airport_system.data.flight

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import ru.nsu.group20211.airport_system.data.Repository
import ru.nsu.group20211.airport_system.data.addJoins
import ru.nsu.group20211.airport_system.data.addOrderBy
import ru.nsu.group20211.airport_system.data.addWhere
import ru.nsu.group20211.airport_system.data.log
import ru.nsu.group20211.airport_system.di.app_module.DatabaseModule
import ru.nsu.group20211.airport_system.domain.employee.models.Brigade
import ru.nsu.group20211.airport_system.domain.employee.models.Brigade.Companion.getInstance
import ru.nsu.group20211.airport_system.domain.flights.models.Airport
import ru.nsu.group20211.airport_system.domain.flights.models.Airport.Companion.getInstance
import ru.nsu.group20211.airport_system.domain.flights.models.ApproximateFlight
import ru.nsu.group20211.airport_system.domain.flights.models.ApproximateFlight.Companion.getInstance
import ru.nsu.group20211.airport_system.domain.flights.models.FlightSchedule
import ru.nsu.group20211.airport_system.domain.flights.models.FlightSchedule.Companion.getInstance
import ru.nsu.group20211.airport_system.domain.plane.models.ModelPlane
import ru.nsu.group20211.airport_system.domain.plane.models.ModelPlane.Companion.getInstance
import ru.nsu.group20211.airport_system.domain.plane.models.Plane
import ru.nsu.group20211.airport_system.domain.plane.models.Plane.Companion.getInstance
import java.sql.Timestamp
import javax.inject.Inject

class FlightScheduleRepository @Inject constructor(
    override val dbContainer: DatabaseModule.DriverContainer
) : Repository<FlightSchedule> {
    override suspend fun getAll(
        listCond: List<String>,
        listOrder: List<String>
    ): List<FlightSchedule> {
        val joinsList = listOf(
            """ LEFT JOIN ${Airport.getTableName()} "departure" ON ("departure"."id" = ${FlightSchedule.getTableName()}."idDepartureAirport") """,
            """ LEFT JOIN ${Airport.getTableName()} "arrival" ON ("arrival"."id" = ${FlightSchedule.getTableName()}."idArrivalAirport") """,
            """ LEFT JOIN ${ApproximateFlight.getTableName()} ON ( ${ApproximateFlight.getTableName()}."id" = ${FlightSchedule.getTableName()}."idApproximateFlights") """,
            """ LEFT JOIN ${Plane.getTableName()} ON (${Plane.getTableName()}."id" = ${FlightSchedule.getTableName()}."plane" ) """,
            """ LEFT JOIN ${Brigade.getTableName()} "pilots" ON ("pilots"."id" = ${FlightSchedule.getTableName()}."brigadePilots" ) """,
            """ LEFT JOIN ${Brigade.getTableName()} "workers" ON ("workers"."id" = ${FlightSchedule.getTableName()}."brigadeWorker" ) """,
            """ LEFT JOIN ${ModelPlane.getTableName()} ON (${ModelPlane.getTableName()}."id" = ${Plane.getTableName()}."model") """,
            """ LEFT JOIN "typesFlights" ON ("flightSchedule"."typeFlight" = "typesFlights"."id") """,
            """ LEFT JOIN "flightStatus" ON ("flightSchedule"."status" = "flightStatus"."id") """
        )
        val listResult = mutableListOf<FlightSchedule>()
        dbContainer.connect().use {
            val result = it.executeQuery(
                (FlightSchedule.getAll() + addJoins(joinsList) + addWhere(listCond) + addOrderBy(
                    listOrder
                )).log()
            )
            while (result.next()) {
                val (flight, index) = result.getInstance(clazz = FlightSchedule::class)
                val (departure, index_2) = result.getInstance(index, clazz = Airport::class)
                val (arrival, index_3) = result.getInstance(index_2, clazz = Airport::class)
                val (approx, index_4) = result.getInstance(
                    index_3,
                    clazz = ApproximateFlight::class
                )
                val (plane, index_5) = result.getInstance(index_4, clazz = Plane::class)
                val (pilots, index_6) = result.getInstance(index_5, Brigade::class)
                val (workers, index_7) = result.getInstance(index_6, Brigade::class)
                val (model, index_8) = result.getInstance(index_7, ModelPlane::class)

                flight.approximateFlight = approx
                flight.arrival = arrival
                flight.departure = departure
                flight.planeEntity = plane
                plane.modelPlane = model
                flight.pilots = pilots
                flight.workers = workers
                listResult.add(flight)
            }
        }
        coroutineScope {
            launch {
                listResult.forEach { data ->
                    launch {
                        dbContainer.connect().use {
                            val result =
                                it.executeQuery(""" SELECT  "flightSchedule"."minNumberTickets" - (SELECT COUNT(*) FROM "tickets" WHERE "idFlight" =  "flightSchedule"."id") AS COUNT_NO_TICKETS FROM "flightSchedule" WHERE "id" = ${data.id} """)
                            if (result.next()) {
                                data.noNeedTickets = result.getInt(1)
                            }
                        }
                        data.noNeedTickets
                    }
                    launch {
                        dbContainer.connect().use {
                            val result =
                                it.executeQuery(""" SELECT  (SELECT COUNT(*) FROM "tickets" WHERE "idFlight" =  "flightSchedule"."id") / "flightSchedule"."minNumberTickets" AS percentage_ratio FROM "flightSchedule" WHERE "id" = ${data.id} """)
                            if (result.next()) {
                                data.procentNoNeedTickets = result.getFloat(1)
                            }
                        }
                        data.noNeedTickets
                    }
                }
            }.join()
        }
        return listResult
    }

    fun getMinTimestamp(): Timestamp {
        return dbContainer.connect().use {
            val result =
                it.executeQuery("""SELECT MIN("takeoffTime") FROM ${FlightSchedule.getTableName()} """.log())
            result.next()
            result.getTimestamp(1)
        }
    }

    fun getMaxTimestamp(): Timestamp {
        return dbContainer.connect().use {
            val result =
                it.executeQuery("""SELECT MAX("takeoffTime") FROM ${FlightSchedule.getTableName()}""".log())
            result.next()
            result.getTimestamp(1)
        }
    }

    fun getMinPrice(): Float {
        return dbContainer.connect().use {
            val result =
                it.executeQuery("""SELECT MIN("price") FROM ${FlightSchedule.getTableName()}""".log())
            result.next()
            result.getFloat(1)
        }
    }

    fun getMaxPrice(): Float {
        return dbContainer.connect().use {
            val result =
                it.executeQuery("""SELECT MAX("price") FROM ${FlightSchedule.getTableName()}""".log())
            result.next()
            result.getFloat(1)
        }
    }
}