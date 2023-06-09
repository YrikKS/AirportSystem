package ru.nsu.group20211.airport_system.data.passengers

import ru.nsu.group20211.airport_system.data.addJoins
import ru.nsu.group20211.airport_system.data.addOrderBy
import ru.nsu.group20211.airport_system.data.addWhere
import ru.nsu.group20211.airport_system.data.log
import ru.nsu.group20211.airport_system.data.Repository
import ru.nsu.group20211.airport_system.di.app_module.DatabaseModule
import ru.nsu.group20211.airport_system.domain.flights.models.FlightSchedule
import ru.nsu.group20211.airport_system.domain.flights.models.FlightSchedule.Companion.getInstance
import ru.nsu.group20211.airport_system.domain.passengers.models.Passenger
import ru.nsu.group20211.airport_system.domain.passengers.models.Passenger.Companion.getInstance
import ru.nsu.group20211.airport_system.domain.passengers.models.TicketsHandedOver
import ru.nsu.group20211.airport_system.domain.passengers.models.TicketsHandedOver.Companion.getInstance
import javax.inject.Inject

class TicketsHandedOverRepository @Inject constructor(
    override val dbContainer: DatabaseModule.DriverContainer
) : Repository<TicketsHandedOver> {

    override suspend fun getAll(
        listCond: List<String>,
        listOrder: List<String>
    ): List<TicketsHandedOver> {
        val listResult = mutableListOf<TicketsHandedOver>()
        var joinList = listOf(
            """ LEFT JOIN ${Passenger.getTableName()} ON (${TicketsHandedOver.getTableName()}."passenger" = ${Passenger.getTableName()}."id") """,
            """ LEFT JOIN ${FlightSchedule.getTableName()} ON (${TicketsHandedOver.getTableName()}."idFlight" = ${FlightSchedule.getTableName()}."id") """
        )
        dbContainer.connect().use {
            val result =
                it.executeQuery(
                    (TicketsHandedOver.getAll() + addJoins(joinList) + addWhere(listCond) + addOrderBy(
                        listOrder
                    )).log()
                )
            while (result.next()) {
                val (ticket, index) = result.getInstance(clazz = TicketsHandedOver::class)
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