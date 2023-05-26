package ru.nsu.group20211.airport_system.data

import entity.log
import ru.nsu.group20211.airport_system.di.app_module.DatabaseModule
import ru.nsu.group20211.airport_system.domain.DbEntity

interface Repository<T : DbEntity> {
    val dbContainer: DatabaseModule.DriverContainer

    suspend fun getAll(
        listCond: List<String> = emptyList(),
        listOrder: List<String> = emptyList()
    ): List<T>

    fun insert(element: T) {
        dbContainer.connect().use {
            it.execute((element.insertQuery()).log())
        }
    }

    fun delete(element: T) {
        dbContainer.connect().use {
            it.execute((element.deleteQuery()).log())
        }
    }

    fun update(element: T) {
        dbContainer.connect().use {
            it.execute((element.updateQuery()).log())
        }
    }
}