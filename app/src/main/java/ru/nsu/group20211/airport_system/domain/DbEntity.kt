package ru.nsu.group20211.airport_system.domain

import java.sql.ResultSet
import kotlin.reflect.KClass

interface DbEntity  {
    fun customGetId() : Int

    fun insertQuery(): String

    fun deleteQuery(): String

    fun updateQuery(): String

    fun myEquals(other: Any) : Boolean
}

interface DbEntityCompanion<T : DbEntity> {
    fun getTableName(): String

    fun getAll() = buildString {
        append("SELECT * FROM ${getTableName()} ")
    }

    fun ResultSet.getInstance(indexStart: Int = 1, clazz: KClass<T>): Pair<T, Int>
}
