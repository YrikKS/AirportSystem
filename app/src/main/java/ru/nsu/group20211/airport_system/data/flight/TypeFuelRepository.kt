package ru.nsu.group20211.airport_system.data.flight

import ru.nsu.group20211.airport_system.data.addOrderBy
import ru.nsu.group20211.airport_system.data.addWhere
import ru.nsu.group20211.airport_system.data.log
import ru.nsu.group20211.airport_system.data.Repository
import ru.nsu.group20211.airport_system.di.app_module.DatabaseModule
import ru.nsu.group20211.airport_system.domain.flights.models.TypeFuel
import ru.nsu.group20211.airport_system.domain.flights.models.TypeFuel.Companion.getInstance
import javax.inject.Inject

class TypeFuelRepository @Inject constructor(
    override val dbContainer: DatabaseModule.DriverContainer
) : Repository<TypeFuel> {
    override suspend fun getAll(listCond: List<String>, listOrder: List<String>): List<TypeFuel> {
        return dbContainer.connect().use {
            val listResult = mutableListOf<TypeFuel>()
            val result =
                it.executeQuery((TypeFuel.getAll() + addWhere(listCond) + addOrderBy(listOrder)).log())
            while (result.next()) {
                listResult.add(result.getInstance(clazz = TypeFuel::class).first)
            }
            listResult
        }
    }
}