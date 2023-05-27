package ru.nsu.group20211.airport_system.data.passengers

import entity.addJoins
import entity.addOrderBy
import entity.addWhere
import entity.log
import ru.nsu.group20211.airport_system.data.Repository
import ru.nsu.group20211.airport_system.di.app_module.DatabaseModule
import ru.nsu.group20211.airport_system.domain.flights.models.FlightSchedule
import ru.nsu.group20211.airport_system.domain.flights.models.FlightSchedule.Companion.getInstance
import ru.nsu.group20211.airport_system.domain.passengers.models.Passenger
import ru.nsu.group20211.airport_system.domain.passengers.models.Passenger.Companion.getInstance
import ru.nsu.group20211.airport_system.domain.passengers.models.Ticket
import ru.nsu.group20211.airport_system.domain.passengers.models.Ticket.Companion.getInstance
import javax.inject.Inject

class TicketRepository @Inject constructor(
    override val dbContainer: DatabaseModule.DriverContainer
) : Repository<Ticket> {
    override suspend fun getAll(listCond: List<String>, listOrder: List<String>): List<Ticket> {
        val listResult = mutableListOf<Ticket>()
        var joinList = listOf(
            """LEFT JOIN ${Passenger.getTableName()} ON (${Ticket.getTableName()}."passenger" = ${Passenger.getTableName()}."id") """,
            """ LEFT JOIN ${FlightSchedule.getTableName()} ON (${Ticket.getTableName()}."idFlight" = ${FlightSchedule.getTableName()}."id") """
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
                ticket.passengerEntity = passenger
                ticket.schedule = schedule
                listResult.add(ticket)
            }
        }
        return listResult
    }
}