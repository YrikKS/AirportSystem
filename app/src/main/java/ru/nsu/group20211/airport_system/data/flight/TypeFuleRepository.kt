package ru.nsu.group20211.airport_system.data.flight

import entity.addOrderBy
import entity.addWhere
import entity.log
import ru.nsu.group20211.airport_system.data.Repository
import ru.nsu.group20211.airport_system.di.app_module.DatabaseModule
import ru.nsu.group20211.airport_system.domain.flights.models.TypeFule
import ru.nsu.group20211.airport_system.domain.flights.models.TypeFule.Companion.getInstance
import javax.inject.Inject

class TypeFuleRepository @Inject constructor(
    override val dbContainer: DatabaseModule.DriverContainer
) : Repository<TypeFule> {
    override suspend fun getAll(listCond: List<String>, listOrder: List<String>): List<TypeFule> {
        return dbContainer.connect().use {
            val listResult = mutableListOf<TypeFule>()
            val result =
                it.executeQuery((TypeFule.getAll() + addWhere(listCond) + addOrderBy(listOrder)).log())
            while (result.next()) {
                listResult.add(result.getInstance(clazz = TypeFule::class).first)
            }
            listResult
        }
    }
}