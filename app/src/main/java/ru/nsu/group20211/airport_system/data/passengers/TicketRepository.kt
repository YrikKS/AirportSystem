package ru.nsu.group20211.airport_system.data.passengers

import ru.nsu.group20211.airport_system.data.Repository
import ru.nsu.group20211.airport_system.data.addJoins
import ru.nsu.group20211.airport_system.data.addOrderBy
import ru.nsu.group20211.airport_system.data.addWhere
import ru.nsu.group20211.airport_system.data.log
import ru.nsu.group20211.airport_system.di.app_module.DatabaseModule
import ru.nsu.group20211.airport_system.domain.flights.models.Airport
import ru.nsu.group20211.airport_system.domain.flights.models.Airport.Companion.getInstance
import ru.nsu.group20211.airport_system.domain.flights.models.FlightSchedule
import ru.nsu.group20211.airport_system.domain.flights.models.FlightSchedule.Companion.getInstance
import ru.nsu.group20211.airport_system.domain.passengers.models.Passenger
import ru.nsu.group20211.airport_system.domain.passengers.models.Passenger.Companion.getInstance
import ru.nsu.group20211.airport_system.domain.passengers.models.Ticket
import ru.nsu.group20211.airport_system.domain.passengers.models.Ticket.Companion.getInstance
import ru.nsu.group20211.airport_system.domain.plane.models.Plane
import ru.nsu.group20211.airport_system.domain.plane.models.Plane.Companion.getInstance
import java.sql.Timestamp
import javax.inject.Inject

class TicketRepository @Inject constructor(
    override val dbContainer: DatabaseModule.DriverContainer
) : Repository<Ticket> {
    override suspend fun getAll(listCond: List<String>, listOrder: List<String>): List<Ticket> {
        val listResult = mutableListOf<Ticket>()
        var joinList = listOf(
            """LEFT JOIN ${Passenger.getTableName()} ON (${Ticket.getTableName()}."passenger" = ${Passenger.getTableName()}."id") """,
            """ LEFT JOIN ${FlightSchedule.getTableName()} ON (${Ticket.getTableName()}."idFlight" = ${FlightSchedule.getTableName()}."id") """,
            """ LEFT JOIN ${Airport.getTableName()} "departure" ON ("departure"."id" = ${FlightSchedule.getTableName()}."idDepartureAirport") """,
            """ LEFT JOIN ${Airport.getTableName()} "arrival" ON ("arrival"."id" = ${FlightSchedule.getTableName()}."idArrivalAirport") """,
            """ LEFT JOIN ${Plane.getTableName()} ON (${Plane.getTableName()}."id" = ${FlightSchedule.getTableName()}."plane" ) """,
        )
        dbContainer.connect().use {
            val result =
                it.executeQuery(
                    (Ticket.getAll() + addJoins(joinList) + addWhere(listCond) + addOrderBy(
                        listOrder
                    )).log()
                )
            while (result.next()) {
                val (ticket, index) = result.getInstance(clazz = Ticket::class)
                val (passenger, index_1) = result.getInstance(index, clazz = Passenger::class)
                val (schedule, index_2) = result.getInstance(index_1, clazz = FlightSchedule::class)
                val (departure, index_3) = result.getInstance(index_2, clazz = Airport::class)
                val (arrival, index_4) = result.getInstance(index_3, clazz = Airport::class)
                val (plane, index_5) = result.getInstance(index_4, clazz = Plane::class)
                schedule.arrival = arrival
                schedule.departure = departure
                schedule.planeEntity = plane
                ticket.passengerEntity = passenger
                ticket.schedule = schedule
                listResult.add(ticket)
            }
        }
        return listResult
    }

    suspend fun getMinPrice(): Float {
        dbContainer.connect().use {
            val result = it.executeQuery(""" SELECT MIN("realPrice(in rubles)") FROM "tickets" """)
            return if (result.next()) {
                result.getFloat(1)
            } else {
                0F
            }
        }
    }

    suspend fun getMaxPrice(): Float {
        dbContainer.connect().use {
            val result = it.executeQuery(""" SELECT MAX("realPrice(in rubles)") FROM "tickets" """)
            return if (result.next()) {
                result.getFloat(1)
            } else {
                Float.MAX_VALUE
            }
        }
    }

    suspend fun getMinTakeOffTIme(): Timestamp {
        dbContainer.connect().use {
            val result = it.executeQuery(""" SELECT MIN("registrationTime") FROM "tickets" """)
            return if (result.next()) {
                result.getTimestamp(1)
            } else {
                Timestamp.valueOf("1999-01-01 00:00:00")
            }
        }
    }

    suspend fun getMaxTakeOffTIme(): Timestamp {
        dbContainer.connect().use {
            val result = it.executeQuery(""" SELECT MAX("registrationTime") FROM "tickets" """)
            return if (result.next()) {
                result.getTimestamp(1)
            } else {
                Timestamp.valueOf("1999-01-01 00:00:00")
            }
        }
    }
}