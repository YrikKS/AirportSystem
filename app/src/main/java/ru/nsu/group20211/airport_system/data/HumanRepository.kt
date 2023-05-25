package ru.nsu.group20211.airport_system.data

import entity.addOrderBy
import entity.addWhere
import entity.log
import ru.nsu.group20211.airport_system.di.app_module.DatabaseModule
import ru.nsu.group20211.airport_system.domain.models.Human
import ru.nsu.group20211.airport_system.domain.models.Human.Companion.getInstance
import javax.inject.Inject

class HumanRepository @Inject constructor(
    private val dbContainer: DatabaseModule.DriverContainer
) {

    fun getHumans(
        listCond: List<String> = emptyList(),
        listOrder: List<String> = emptyList()
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

    fun insertHuman(
        human: Human,
    ) {
        dbContainer.connect().use {
            it.execute((human.insertQuery()).trim().log())
        }
    }

    fun updateHuman(human: Human) {
        dbContainer.connect().use {
            it.execute((human.updateQuery()).log())
        }
    }

    fun deleteHuman(human: Human) {
        dbContainer.connect().use {
            it.execute((human.deleteQuery()).log())
        }
    }

}