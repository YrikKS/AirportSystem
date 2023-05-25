package ru.nsu.group20211.airport_system.presentation

import ru.nsu.group20211.airport_system.domain.models.DbEntity

interface BaseDbViewModel {
    fun getData(
        listCond: List<String> = emptyList(),
        listOrder: List<String> = emptyList()
    )

    fun delete(dbEntity: DbEntity)
    fun insert(dbEntity: DbEntity)
    fun update(dbEntity: DbEntity)
}