package ru.nsu.group20211.airport_system.data.passengers

import entity.addOrderBy
import entity.addWhere
import entity.log
import ru.nsu.group20211.airport_system.data.Repository
import ru.nsu.group20211.airport_system.di.app_module.DatabaseModule
import ru.nsu.group20211.airport_system.domain.passengers.models.Passenger
import ru.nsu.group20211.airport_system.domain.passengers.models.Passenger.Companion.getInstance
import javax.inject.Inject

class PassengerRepository @Inject constructor(
    override val dbContainer: DatabaseModule.DriverContainer
) : Repository<Passenger> {
    override suspend fun getAll(listCond: List<String>, listOrder: List<String>): List<Passenger> {
        val listResult = mutableListOf<Passenger>()
        dbContainer.connect()
            .use {
                val result = it.executeQuery(
                    (Passenger.getAll() + addWhere(listCond) + addOrderBy(listOrder)).log()
                )
                while (result.next()) {
                    listResult.add(result.getInstance(clazz = Passenger::class).first)
                }
            }
        return listResult
    }
}