package ru.nsu.group20211.airport_system.data.flight

import ru.nsu.group20211.airport_system.data.addJoins
import ru.nsu.group20211.airport_system.data.addOrderBy
import ru.nsu.group20211.airport_system.data.addWhere
import ru.nsu.group20211.airport_system.data.log
import ru.nsu.group20211.airport_system.data.Repository
import ru.nsu.group20211.airport_system.di.app_module.DatabaseModule
import ru.nsu.group20211.airport_system.domain.flights.models.Airport
import ru.nsu.group20211.airport_system.domain.flights.models.Airport.Companion.getInstance
import ru.nsu.group20211.airport_system.domain.flights.models.ApproximateFlight
import ru.nsu.group20211.airport_system.domain.flights.models.ApproximateFlight.Companion.getInstance
import javax.inject.Inject

class ApproximateFlightRepository @Inject constructor(
    override val dbContainer: DatabaseModule.DriverContainer
) : Repository<ApproximateFlight> {
    override suspend fun getAll(
        listCond: List<String>,
        listOrder: List<String>
    ): List<ApproximateFlight> {
        val resultList = mutableListOf<ApproximateFlight>()
        val listJoins = listOf(
            """ LEFT JOIN ${Airport.getTableName()} "departure" ON ("departure"."id" = ${ApproximateFlight.getTableName()}."idDepartureAirport") """,
            """ LEFT JOIN ${Airport.getTableName()} "arrival" ON ("arrival"."id" = ${ApproximateFlight.getTableName()}."idArrivalAirport") """
        )
        dbContainer.connect().use {
            val result = it.executeQuery(
                (ApproximateFlight.getAll() + addJoins(listJoins) + addWhere(listCond) + addOrderBy(
                    listOrder
                )).log()
            )
            while (result.next()) {
                val (schedule, index) = result.getInstance(clazz = ApproximateFlight::class)
                val (departure, index_2) = result.getInstance(index, clazz = Airport::class)
                val (arrival, index_3) = result.getInstance(index_2, clazz = Airport::class)
                schedule.departureAirport = departure
                schedule.arrivalAirport = arrival
                resultList.add(schedule)
            }
        }
        return resultList
    }


    suspend fun getAirports(): List<Airport> {
        return dbContainer.connect().use {
            val listResult = mutableListOf<Airport>()
            val result = it.executeQuery(Airport.getAll().log())
            while (result.next()) {
                listResult.add(result.getInstance(clazz = Airport::class).first)
            }
            listResult
        }
    }
}