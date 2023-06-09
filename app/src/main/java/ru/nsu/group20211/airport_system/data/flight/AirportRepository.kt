package ru.nsu.group20211.airport_system.data.flight

import ru.nsu.group20211.airport_system.data.addOrderBy
import ru.nsu.group20211.airport_system.data.addWhere
import ru.nsu.group20211.airport_system.data.log
import ru.nsu.group20211.airport_system.data.Repository
import ru.nsu.group20211.airport_system.di.app_module.DatabaseModule
import ru.nsu.group20211.airport_system.domain.flights.models.Airport
import ru.nsu.group20211.airport_system.domain.flights.models.Airport.Companion.getInstance
import javax.inject.Inject

class AirportRepository @Inject constructor(
    override val dbContainer: DatabaseModule.DriverContainer
) : Repository<Airport> {
    override suspend fun getAll(listCond: List<String>, listOrder: List<String>): List<Airport> {
        val resultList = mutableListOf<Airport>()
        dbContainer.connect().use {
            val result = it.executeQuery(
                (Airport.getAll() + addWhere(listCond) + addOrderBy(listOrder)).log()
            )
            while (result.next()) {
                resultList.add(result.getInstance(clazz = Airport::class).first)
            }
        }
        return resultList
    }
}