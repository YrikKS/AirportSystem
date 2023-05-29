package ru.nsu.group20211.airport_system.data.employee

import ru.nsu.group20211.airport_system.data.addOrderBy
import ru.nsu.group20211.airport_system.data.addWhere
import ru.nsu.group20211.airport_system.data.log
import ru.nsu.group20211.airport_system.data.Repository
import ru.nsu.group20211.airport_system.di.app_module.DatabaseModule
import ru.nsu.group20211.airport_system.domain.employee.models.Human
import ru.nsu.group20211.airport_system.domain.employee.models.Human.Companion.getInstance
import javax.inject.Inject

class HumanRepository @Inject constructor(
    override val dbContainer: DatabaseModule.DriverContainer
) : Repository<Human> {

    override suspend fun getAll(
        listCond: List<String>,
        listOrder: List<String>
    ): List<Human> {
        val listResult = mutableListOf<Human>()
        dbContainer.connect()
            .use {
                val result = it.executeQuery(
                    (Human.getAll() + addWhere(listCond) + addOrderBy(listOrder)).log()
                )
                while (result.next()) {
                    listResult.add(result.getInstance(clazz = Human::class).first)
                }
            }
        return listResult
    }
}